/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.livedata.server;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.opengamma.util.ArgumentChecker;

/**
 * 
 * 
 * @author pietari
 */
@ManagedResource(
    objectName="com.opengamma:name=PersistentSubscriptionManager",
    description="This MBean is used to manage persistent market data subscriptions on a Live Data server." +
		" Persistent subscriptions do not expire, and survive even server restarts.")
public class PersistentSubscriptionManagerMBean {
  
  private static final Logger s_logger = LoggerFactory.getLogger(PersistentSubscriptionManagerMBean.class);
  private final AbstractPersistentSubscriptionManager _manager;
  
  public PersistentSubscriptionManagerMBean(AbstractPersistentSubscriptionManager manager) {
    ArgumentChecker.checkNotNull(manager, "Persistent subscription manager");   
    _manager = manager;
  }
  
  @ManagedAttribute(description="Returns the list of securities for which a persistent subscription is currently active.")
  public Set<String> getPersistentSubscriptions() {
    try {
      return _manager.getPersistentSubscriptions();
    } catch (RuntimeException e) {
      s_logger.error("getPersistentSubscriptions() failed", e);
      throw new RuntimeException(e.getMessage());
    }
  }

  @ManagedOperation(description = "Reads persistent subscriptions from persistent storage."
      + " Subscribes to any entries to which we are not yet subscribed.")
  public void refresh() {
    try {
      _manager.refresh();
    } catch (RuntimeException e) {
      s_logger.error("refresh() failed", e);
      throw new RuntimeException(e.getMessage());
    }
  }

  @ManagedOperation(description = "Saves all persistent subscriptions to persistent storage.")
  public void save() {
    try {
      _manager.save();
    } catch (RuntimeException e) {
      s_logger.error("save() failed", e);
      throw new RuntimeException(e.getMessage());
    }
  }
  
  @ManagedOperation(description = "Adds a persistent subscription. If the subscription already exists, makes it persistent.")
  @ManagedOperationParameters({
      @ManagedOperationParameter(name = "securityUniqueId", description = "Security unique ID. Server type dependent.)")})
  public void addPersistentSubscription(String securityUniqueId) {
    try {
      _manager.addPersistentSubscription(securityUniqueId);
    } catch (RuntimeException e) {
      s_logger.error("addPersistentSubscription(" + securityUniqueId + ")  failed", e);
      throw new RuntimeException(e.getMessage());
    }
  }
  
  @ManagedOperation(description = "Removes a persistent subscription by making the subscription non-persistent." +
  		" Returns true if a subscription was actually made non-persistent, false otherwise.")
	@ManagedOperationParameters({
      @ManagedOperationParameter(name = "securityUniqueId", description = "Security unique ID. Server type dependent.)")})
  public boolean removePersistentSubscription(String securityUniqueId) {
    try {
      return _manager.removePersistentSubscription(securityUniqueId);
    } catch (RuntimeException e) {
      s_logger.error("removePersistentSubscription(" + securityUniqueId + ") failed", e);
      throw new RuntimeException(e.getMessage());
    }
  }

}
