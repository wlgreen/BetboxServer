/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.betbox.server;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Context initializer that loads the API key from the App Engine datastore.
 */
public class ApiKeyInitializer implements ServletContextListener {

	static final String ATTRIBUTE_ACCESS_KEY = "apiKey";

	private static final String ENTITY_KIND = "Settings";
	private static final String ENTITY_KEY = "MyKey";
	private static final String ACCESS_KEY_FIELD = "ApiKey";

	private final Logger logger = Logger.getLogger(getClass().getName());

	public void contextInitialized(ServletContextEvent event) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey(ENTITY_KIND, ENTITY_KEY);

		Entity entity;
		try {
			entity = datastore.get(key);
		} catch (EntityNotFoundException e) {
			entity = new Entity(key);
			// NOTE: it's not possible to change entities in the local server,
			// so
			// it will be necessary to hardcode the API key below if you are
			// running
			// it locally.
			entity.setProperty(ACCESS_KEY_FIELD,
					"AIzaSyBx0XAt651Isdfz45Xt5q85-XIbPk_99T4");
			datastore.put(entity);
			logger.severe("Created fake key. Please go to App Engine admin "
					+ "console, change its value to your API Key (the entity "
					+ "type is '" + ENTITY_KIND
					+ "' and its field to be changed is '" + ACCESS_KEY_FIELD
					+ "'), then restart the server!");
		}
		String accessKey = (String) entity.getProperty(ACCESS_KEY_FIELD);
		event.getServletContext().setAttribute(ATTRIBUTE_ACCESS_KEY, accessKey);
	}

	public void contextDestroyed(ServletContextEvent event) {
	}

}
