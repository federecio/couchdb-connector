/**
 * Mule Development Kit
 * Copyright 2010-2011 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This file was automatically generated by the Mule Development Kit
 */
package org.mule.modules.couchdb;

import com.couchbase.client.CouchbaseClient;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Apache CouchDB Connector.
 *
 * @author Ryan Meyer
 * @author Mulesoft, inc.
 */
@Connector(name = "couchdb")
public class CouchDBConnector {

    /**
     * the URI list of one or more servers from the cluster
     */
    @Configurable
    private List<URI> uris;

    /**
     * The amount of time time for shutdown. If not set shutdown will start immediately.
     */
    @Configurable
    @Optional
    @Default("-1")
    private long shutdownTimeout;

    /**
     * the TimeUnit for the shutdown timeout. Only considered if {@link this#shutdownTimeUnit} is specified.
     */
    @Configurable
    @Optional
    @Default("MILLISECONDS")
    private TimeUnit shutdownTimeUnit;

    private CouchbaseClient client;

    private String bucketName;

    /**
     * Establishes the database connection using the given bucket name and password.
     *
     * @param bucketName the bucket name in the cluster you wish to use
     * @param password   the password for the bucket
     * @throws ConnectionException
     */
    @Connect
    public void connect(@ConnectionKey String bucketName, String password) throws ConnectionException {
        try {
            this.bucketName = bucketName;
            client = new CouchbaseClient(uris, bucketName, password);
        } catch (IOException e) {
            throw new ConnectionException(ConnectionExceptionCode.UNKNOWN, "", "Failed to create com.couchbase.client.CouchbaseClient", e);
        }

    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        client.shutdown(shutdownTimeout, shutdownTimeUnit);
        client = null;
    }

    /**
     * Are we connected
     */
    @ValidateConnection
    public boolean isConnected() {
        return client != null;
    }

    /**
     * Are we connected
     */
    @ConnectionIdentifier
    public String connectionId() {
        return "Bucket name: " + bucketName;
    }

    /**
     * Get with a single key and decode using the default transcoder.
     * <p/>
     * {@sample.xml ../../../doc/couchdb-connector.xml.sample couchdb:get}
     *
     * @return Some string
     */
    @Processor
    public Object get(String key) {
        return client.get(key);
    }

    public void setUris(List<URI> uris) {
        this.uris = uris;
    }

    public void setShutdownTimeout(long shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    public void setShutdownTimeUnit(TimeUnit shutdownTimeUnit) {
        this.shutdownTimeUnit = shutdownTimeUnit;
    }
}