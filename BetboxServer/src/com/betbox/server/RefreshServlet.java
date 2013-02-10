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

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.betbox.server.data.Bet;
import com.betbox.server.data.Stand;
import com.betbox.server.data.Util;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * Servlet that adds display number of devices and button to send a message.
 * <p>
 * This servlet is used just by the browser (i.e., not device) and contains the
 * main page of the demo app.
 */
@SuppressWarnings("serial")
public class RefreshServlet extends BaseServlet {
    private static final Logger log = Logger.getLogger("administation");

	static final String ATTRIBUTE_STATUS = "status";
	static final String RESPONSE_SUCCESS = "SUCCESS";
	static final String RESPONSE_FAILURE = "FAILURE";

	/**
	 * Displays the existing messages and offer the option to send a new one.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException  {
		log.info("Got refresh request");
		
        String time = req.getParameter("last bet received");
        String device = req.getParameter("device");
        
        /* send all bet temporarily. Need to record the last update time for better sync up */
        Iterable<Entity> entities = Bet.getAllBets();
        if (entities != null) {
			Queue queue = QueueFactory.getDefaultQueue();
			TaskOptions taskOptions = TaskOptions.Builder.withUrl("/send")
					.param(SendMessageServlet.PARAMETER_DEVICE, device)
					.param(Datastore.PARAMETER_JSON, Util.writeJSON(entities));
			queue.add(taskOptions);
    		resp.getWriter().print(RESPONSE_SUCCESS);
        } else {
    		resp.getWriter().print(RESPONSE_FAILURE);
        }
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}

}
