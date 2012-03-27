/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup.html.image.resource;

import java.util.Locale;

import javax.annotation.Nullable;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.IResourceFactory;
import org.apache.wicket.IResourceListener;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.IResource.Attributes;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.parse.metapattern.Group;
import org.apache.wicket.util.parse.metapattern.MetaPattern;
import org.apache.wicket.util.parse.metapattern.OptionalMetaPattern;
import org.apache.wicket.util.parse.metapattern.parsers.MetaPatternParser;
import org.apache.wicket.util.string.Strings;


/**
 * THIS CLASS IS INTENDED FOR INTERNAL USE IN IMPLEMENTING LOCALE SENSITIVE COMPONENTS THAT USE
 * IMAGE RESOURCES AND SHOULD NOT BE USED DIRECTLY BY END-USERS.
 * <p>
 * This class contains the logic for extracting static image resources referenced by the SRC
 * attribute of component tags and keeping these static image resources in sync with the component
 * locale.
 * <p>
 * If no image is specified by the SRC attribute of an IMG tag, then any VALUE attribute is
 * inspected. If there is a VALUE attribute, it must be of the form
 * "[factoryName]:[sharedImageName]?:[specification]". [factoryName] is the name of a resource
 * factory that has been added to Application (for example, DefaultButtonImageResourceFactory is
 * installed by default under the name "buttonFactory"). The [sharedImageName] value is optional and
 * gives a name under which a given generated image is shared. For example, a cancel button image
 * generated by the VALUE attribute "buttonFactory:cancelButton:Cancel" is shared under the name
 * "cancelButton" and this specification will cause a component to reference the same image resource
 * no matter what page it appears on, which is a very convenient and efficient way to create and
 * share images. The [specification] string which follows the second colon is passed directly to the
 * image factory and its format is dependent on the specific image factory. For details on the
 * default buttonFactory, see
 * {@link org.apache.wicket.markup.html.image.resource.DefaultButtonImageResourceFactory}.
 * <p>
 * Finally, if there is no SRC attribute and no VALUE attribute, the Image component's model is
 * inspected. If the model contains a resource or resource reference, this image is used, otherwise
 * the model is converted to a String and that value is used as a path to load the image.
 * 
 * @author Jonathan Locke
 */
public final class LocalizedImageResource implements IClusterable
{
	private static final long serialVersionUID = 1L;

	/**
	 * What kind of resource it is. TRUE==Resource is set, FALSE==ResourceReference is set, null
	 * none
	 */
	private Boolean resourceKind;

	/** The component that is referencing this image resource */
	private final Component component;

	/** The image resource this image component references */
	private IResource resource;

	/** The resource reference */
	private ResourceReference resourceReference;

	/** The resource parameters */
	private PageParameters resourceParameters;

	/** The locale of the image resource */
	private Locale locale;

	/** The style of the image resource */
	private String style;

	/** The component's variation (of the style) */
	private String variation;

	/**
	 * Parses image value specifications of the form "[factoryName]:
	 * [shared-image-name]?:[specification]"
	 * 
	 * @author Jonathan Locke
	 */
	private static final class ImageValueParser extends MetaPatternParser
	{
		/** Factory name */
		private static final Group factoryName = new Group(MetaPattern.VARIABLE_NAME);

		/** Image reference name */
		private static final Group imageReferenceName = new Group(MetaPattern.VARIABLE_NAME);

		/** Factory specification string */
		private static final Group specification = new Group(MetaPattern.ANYTHING_NON_EMPTY);

		/** Meta pattern. */
		private static final MetaPattern pattern = new MetaPattern(factoryName, MetaPattern.COLON,
			new OptionalMetaPattern(new MetaPattern[] { imageReferenceName }), MetaPattern.COLON,
			specification);

		/**
		 * Construct.
		 * 
		 * @param input
		 *            to parse
		 */
		private ImageValueParser(final CharSequence input)
		{
			super(pattern, input);
		}

		/**
		 * @return The factory name
		 */
		private String getFactoryName()
		{
			return factoryName.get(matcher());
		}

		/**
		 * @return Returns the imageReferenceName.
		 */
		private String getImageReferenceName()
		{
			return imageReferenceName.get(matcher());
		}

		/**
		 * @return Returns the specification.
		 */
		private String getSpecification()
		{
			return specification.get(matcher());
		}
	}

	/**
	 * Constructor
	 * 
	 * @param component
	 *            The component that owns this localized image resource
	 */
	public LocalizedImageResource(final Component component)
	{
		this.component = component;
		locale = component.getLocale();
		style = component.getStyle();
		variation = component.getVariation();
	}

	/**
	 * Binds this resource if it is shared
	 */
	public final void bind()
	{
		// If we have a resource reference
		if (resourceReference != null && resourceReference.canBeRegistered() &&
			Application.exists())
		{
			// Bind the reference to the application
			Application.get()
				.getResourceReferenceRegistry()
				.registerResourceReference(resourceReference);
		}
	}

	/**
	 * @param parameters
	 *            page parameters
	 */
	public final void onResourceRequested(@Nullable PageParameters parameters)
	{
		bind();
		RequestCycle requestCycle = RequestCycle.get();
		Attributes attributes = new Attributes(requestCycle.getRequest(),
			requestCycle.getResponse(), parameters);
		resource.respond(attributes);
	}

	/**
	 * @param resource
	 *            The resource to set.
	 */
	public final void setResource(@Nullable final IResource resource)
	{
		if (this.resource != resource)
		{
			resourceKind = Boolean.TRUE;
			this.resource = resource;
		}
	}

	/**
	 * @param resourceReference
	 *            The resource to set.
	 */
	public final void setResourceReference(@Nullable final ResourceReference resourceReference)
	{
		setResourceReference(resourceReference, resourceParameters);
	}

	/**
	 * @return true if it has a resourceReference. (it points to a shared resource)
	 */
	public final boolean isStateless()
	{
		return resourceReference != null;
	}

	/**
	 * @param resourceReference
	 *            The resource to set.
	 * @param resourceParameters
	 *            The resource parameters for the shared resource
	 */
	public final void setResourceReference(final ResourceReference resourceReference,
		final PageParameters resourceParameters)
	{
		if (resourceReference != this.resourceReference)
		{
			resourceKind = Boolean.FALSE;
			this.resourceReference = resourceReference;
		}
		this.resourceParameters = resourceParameters;
		bind();
	}

	/**
	 * @param tag
	 *            The tag to inspect for an optional src attribute that might reference an image.
	 * @throws WicketRuntimeException
	 *             Thrown if an image is required by the caller, but none can be found.
	 */
	public final void setSrcAttribute(final ComponentTag tag)
	{
		// If locale has changed from the initial locale used to attach image
		// resource, then we need to reload the resource in the new locale
		Locale l = component.getLocale();
		String s = component.getStyle();
		String v = component.getVariation();
		if (resourceKind == null &&
			(!Objects.equal(locale, l) || !Objects.equal(style, s) || !Objects.equal(variation, v)))
		{
			// Get new component locale and style
			locale = l;
			style = s;
			variation = v;

			// Invalidate current resource so it will be reloaded/recomputed
			resourceReference = null;
			resource = null;
		}
		else
		{
			// TODO post 1.2: should we have support for locale changes when the
			// resource reference (or resource??) is set manually..
			// We should get a new resource reference for the current locale
			// then that points to the same resource but with another locale if
			// it exists. Something like
			// SharedResource.getResourceReferenceForLocale(resourceReference);
		}

		// check if the model contains a resource, if so, load the resource from
		// the model.
		Object modelObject = component.getDefaultModelObject();
		if (modelObject instanceof ResourceReference)
		{
			resourceReference = (ResourceReference)modelObject;
		}
		else if (modelObject instanceof IResource)
		{
			resource = (IResource)modelObject;
		}

		// Need to load image resource for this component?
		if (resource == null && resourceReference == null)
		{
			// Get SRC attribute of tag
			final CharSequence src = tag.getAttribute("src");
			if (src != null)
			{
				// Try to load static image
				loadStaticImage(src.toString());
			}
			else
			{
				// Get VALUE attribute of tag
				final CharSequence value = tag.getAttribute("value");
				if (value != null)
				{
					// Try to generate an image using an image factory
					newImage(value);
				}
				else
				{
					// Load static image using model object as the path
					loadStaticImage(component.getDefaultModelObjectAsString());
				}
			}
		}

		// Get URL for resource
		final CharSequence url;
		if (resourceReference != null)
		{
			// Create URL to resource
			url = RequestCycle.get().urlFor(resourceReference, resourceParameters);
		}
		else
		{
			// Create URL to component
			url = component.urlFor(IResourceListener.INTERFACE, resourceParameters);
		}

		// Set the SRC attribute to point to the component or shared resource
		tag.put("src", url);
	}

	/**
	 * @param application
	 *            The application
	 * @param factoryName
	 *            The name of the image resource factory
	 * @return The resource factory
	 * @throws WicketRuntimeException
	 *             Thrown if factory cannot be found
	 */
	private IResourceFactory getResourceFactory(final Application application,
		final String factoryName)
	{
		final IResourceFactory factory = application.getResourceSettings().getResourceFactory(
			factoryName);

		// Found factory?
		if (factory == null)
		{
			throw new WicketRuntimeException("Could not find image resource factory named " +
				factoryName);
		}
		return factory;
	}


	static class SimpleStaticResourceReference extends ResourceReference
	{
		final IResource resource;

		public SimpleStaticResourceReference(Class<?> scope, String name, Locale locale,
			String style, String variation, IResource resource)
		{
			super(scope, name, locale, style, variation);
			this.resource = resource;
		}

		private static final long serialVersionUID = 1L;

		@Override
		public IResource getResource()
		{
			return resource;
		}

	}

	/**
	 * Tries to load static image at the given path and throws an exception if the image cannot be
	 * located.
	 * 
	 * @param path
	 *            The path to the image
	 * @throws WicketRuntimeException
	 *             Thrown if the image cannot be located
	 */
	private void loadStaticImage(final String path)
	{
		MarkupContainer parent = component.findParentWithAssociatedMarkup();
		if (parent instanceof Border)
		{
			parent = parent.getParent();
		}
		final Class<?> scope = parent.getClass();
		resourceReference = new PackageResourceReference(scope, path, locale, style, variation);
		bind();
	}

	/**
	 * Generates an image resource based on the attribute values on tag
	 * 
	 * @param value
	 *            The value to parse
	 */
	private void newImage(final CharSequence value)
	{
		// Parse value
		final ImageValueParser valueParser = new ImageValueParser(value);

		// Does value match parser?
		if (valueParser.matches())
		{
			final String imageReferenceName = valueParser.getImageReferenceName();
			final String specification = Strings.replaceHtmlEscapeNumber(valueParser.getSpecification());
			final String factoryName = valueParser.getFactoryName();
			final Application application = component.getApplication();

			// Do we have a reference?
			if (!Strings.isEmpty(imageReferenceName))
			{
				// Is resource already available via the application?
				if (application.getResourceReferenceRegistry().getResourceReference(
					Application.class, imageReferenceName, locale, style, variation, true, false) == null)
				{
					// Resource not available yet, so create it with factory and
					// share via Application
					final IResource imageResource = getResourceFactory(application, factoryName).newResource(
						specification, locale, style, variation);

					ResourceReference ref = new SimpleStaticResourceReference(Application.class,
						imageReferenceName, locale, style, variation, imageResource);

					application.getResourceReferenceRegistry().registerResourceReference(ref);
				}

				// Create resource reference
				resourceReference = new PackageResourceReference(Application.class,
					imageReferenceName, locale, style, variation);
			}
			else
			{
				resource = getResourceFactory(application, factoryName).newResource(specification,
					locale, style, variation);
			}
		}
		else
		{
			throw new WicketRuntimeException(
				"Could not generate image for value attribute '" +
					value +
					"'.  Was expecting a value attribute of the form \"[resourceFactoryName]:[resourceReferenceName]?:[factorySpecification]\".");
		}
	}

	/**
	 * return the resource
	 * 
	 * @return resource or <code>null</code> if there is none
	 */
	public final IResource getResource()
	{
		return resource;
	}

	/**
	 * return the resource
	 * 
	 * @return resource or <code>null</code> if there is none
	 */
	public final ResourceReference getResourceReference()
	{
		return resourceReference;
	}
}
