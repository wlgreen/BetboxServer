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
import com.google.appengine.api.datastore.Entity;

/**
 * Servlet that adds display number of devices and button to send a message.
 * <p>
 * This servlet is used just by the browser (i.e., not device) and contains the
 * main page of the demo app.
 */
@SuppressWarnings("serial")
public class CheckOutBetServlet extends BaseServlet {
	private static final Logger log = Logger.getLogger("administation");

	/**
	 * Displays the existing messages and offer the option to send a new one.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		log.info("Checkout Bet");
		String content = req.getParameter("content");
		String status;
		if (content != null && content != "") {
			Entity entity = Bet.getSingleBet(content);
			if (entity != null) {
				Bet bet = Bet.makeBet(content);
				if (bet.status.equals(Bet.STATUS_OPEN)) {
					bet.status = Bet.STATUS_CLOSE;
					Bet.saveBet(bet);
					status = Datastore.broadcast(bet);
					req.setAttribute(CreateBetServlet.ATTRIBUTE_STATUS,
							status.toString());
				}
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
