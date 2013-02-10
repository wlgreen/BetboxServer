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
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * Servlet that create a new bet.
 * Nothing will change if the bet already exists
 */
@SuppressWarnings("serial")
public class CreateBetServlet extends BaseServlet {
	private static final Logger log = Logger.getLogger("administation");

	static final String ATTRIBUTE_STATUS = "status";

	/**
	 * Displays the existing messages and offer the option to send a new one.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Bet bet = null;
		String status;

		String content = req.getParameter("content");
		if (content != null && content != "") {
			/* Store this bet into the datastore */
			Entity entity = Bet.getSingleBet(content);
			if (entity != null) {
				log.info("This bet has already been entered " + content);
			} else {
				bet = Bet.makeBet(content);

				if (user != null && bet != null) {
					log.info("New betting posted by user " + user.getNickname()
							+ ": " + content);
				} else {
					log.info("New betting posted anonymously: " + content);
				}

				status = Datastore.broadcast(bet);
				req.setAttribute(CreateBetServlet.ATTRIBUTE_STATUS,
						status.toString());
			}
		}
		getServletContext().getRequestDispatcher("/main.jsp")
				.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}

}
