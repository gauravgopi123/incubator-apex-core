/*
 *  Copyright (c) 2012 Malhar, Inc.
 *  All Rights Reserved.
 */
package com.malhartech.bufferserver.client;

import com.malhartech.bufferserver.packet.MessageType;
import com.malhartech.bufferserver.packet.PurgeRequestTuple;
import com.malhartech.bufferserver.packet.ResetRequestTuple;
import com.malhartech.bufferserver.packet.Tuple;
import java.io.IOException;
import malhar.netlet.DefaultEventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Chetan Narsude <chetan@malhar-inc.com>
 */
public abstract class Controller extends AbstractClient
{
  String id;

  public Controller(String id)
  {
    super(1024, 1024);
    this.id = id;
  }

  public void purge(String sourceId, long windowId)
  {
    logger.debug("sending purge request sourceId = {}, windowId = {}", sourceId, windowId);
    write(PurgeRequestTuple.getSerializedRequest(sourceId, windowId));
  }

  public void reset(String sourceId, long windowId)
  {
    logger.debug("sending reset request sourceId = {}, windowId = {}", sourceId, windowId);
    write(ResetRequestTuple.getSerializedRequest(sourceId, windowId));
  }

  @Override
  public void onMessage(byte[] buffer, int offset, int size)
  {
    Tuple t = Tuple.getTuple(buffer, offset, size);
    assert (t.getType() == MessageType.PAYLOAD);
    Fragment f = t.getData();
    onMessage(new String(f.buffer, f.offset, f.length));
  }

  public abstract void onMessage(String message);

  @Override
  public String toString()
  {
    return "Controller{" + "id=" + id + '}';
  }

  private static final Logger logger = LoggerFactory.getLogger(Controller.class);
}