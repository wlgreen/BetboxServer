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

import com.betbox.server.data.Stand;

/**
 * Servlet that receives the update request from the device.
 */
@SuppressWarnings("serial")
public class UpdateBetServlet extends BaseServlet {
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
    	log.info("Update bet get called");
        String betContent = req.getParameter("bet");
        String standContent = req.getParameter("stand");
        String memberID = req.getParameter("member");
        Stand stand;
        
        stand = Stand.createOrUpdateStand(betContent, memberID, standContent);
        if (stand != null) {
        	log.info("New or updated stand (" + standContent + ") posted by user " + memberID + ": " + betContent);
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
