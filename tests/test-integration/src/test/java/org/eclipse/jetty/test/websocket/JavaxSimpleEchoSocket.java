//
// ========================================================================
// Copyright (c) 1995-2020 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under
// the terms of the Eclipse Public License 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0
//
// This Source Code may also be made available under the following
// Secondary Licenses when the conditions for such availability set
// forth in the Eclipse Public License, v. 2.0 are satisfied:
// the Apache License v2.0 which is available at
// https://www.apache.org/licenses/LICENSE-2.0
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.test.websocket;

import java.util.concurrent.CountDownLatch;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import static org.junit.jupiter.api.Assertions.fail;

@ClientEndpoint(
    subprotocols = {"chat"})
public class JavaxSimpleEchoSocket
{
    private static final Logger LOG = Log.getLogger(JavaxSimpleEchoSocket.class);
    private Session session;
    public CountDownLatch messageLatch = new CountDownLatch(1);
    public CountDownLatch closeLatch = new CountDownLatch(1);

    @OnError
    public void onError(Throwable t)
    {
        LOG.warn(t);
        fail(t.getMessage());
    }

    @OnClose
    public void onClose(CloseReason close)
    {
        LOG.debug("Closed: {}, {}", close.getCloseCode().getCode(), close.getReasonPhrase());
        closeLatch.countDown();
    }

    @OnMessage
    public void onMessage(String message)
    {
        LOG.debug("Received: {}", message);
        messageLatch.countDown();
    }

    @OnOpen
    public void onOpen(Session session)
    {
        LOG.debug("Opened");
        this.session = session;
    }
}