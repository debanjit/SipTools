/*
 * Copyright 2009 Charles Chappell.
 *
 * This file is part of IcedJava.
 *
 * IcedJava is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * IcedJava is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with IcedJava.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package net.mc_cubed.icedjava.ice;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;
import net.mc_cubed.icedjava.stun.DemultiplexerSocket;
import net.mc_cubed.icedjava.stun.TransportType;

/**
 * Represents a local candidate with a channel that can be accessed and used for
 * various purposes
 *
 * @author Charles Chappell
 * @since 0.9
 */
public class LocalCandidate extends Candidate {
    private final CandidateType type;
    private final InetAddress address;
    private final int port;
    private long priority;
    private final short componentId;
    private final TransportType transport;


    InetSocketAddress stunServer;
    DemultiplexerSocket socket;
    private final IcePeer owner;
    private final IceSocket iceSocket;

    protected Date nextKeepalive;

    /**
     * Get the value of nextKeepalive
     *
     * @return the value of nextKeepalive
     */
    public Date getNextKeepalive() {
        return nextKeepalive;
    }

    /**
     * Set the value of nextKeepalive
     *
     * @param nextKeepalive new value of nextKeepalive
     */
    public void setNextKeepalive(Date nextKeepalive) {
        this.nextKeepalive = nextKeepalive;
    }

    protected KeepaliveHandler keepaliveHandler;

    /**
     * Get the value of keepaliveHandler
     *
     * @return the value of keepaliveHandler
     */
    public KeepaliveHandler getKeepaliveHandler() {
        return keepaliveHandler;
    }

    /**
     * Set the value of keepaliveHandler
     *
     * @param keepaliveHandler new value of keepaliveHandler
     */
    public void setKeepaliveHandler(KeepaliveHandler keepaliveHandler) {
        this.keepaliveHandler = keepaliveHandler;
    }

    protected Object keepaliveObjectData;

    /**
     * Get the value of keepaliveObjectData
     *
     * @return the value of keepaliveObjectData
     */
    public Object getKeepaliveObjectData() {
        return keepaliveObjectData;
    }

    /**
     * Set the value of keepaliveObjectData
     *
     * @param keepaliveObjectData new value of keepaliveObjectData
     */
    public void setKeepaliveObjectData(Object keepaliveObjectData) {
        this.keepaliveObjectData = keepaliveObjectData;
    }

    @Override
    public final String getFoundation() {
        // The foundation computation is defined in 4.1.1.3
        String foundation = getType().toString() + ":" + getAddress().toString() + ":" + stunServer + ":" + socket.getTransportType();

        // Use a hashcode of the above to shorten, and not leak too much information
        return String.valueOf(Math.abs(foundation.hashCode()));
    }
    
    public LocalCandidate(IcePeer owner, IceSocket iceSocket, CandidateType type, DemultiplexerSocket socket) {
        this(owner, iceSocket, type, socket, (short) 0);
    }

    public LocalCandidate(IcePeer owner, IceSocket iceSocket, CandidateType type, DemultiplexerSocket socket, short componentId) {
        this.type = type;
        this.address = socket.getLocalAddress();
        this.port = socket.getLocalPort();
        this.componentId = componentId;
        this.transport = socket.getTransportType();
        this.socket = socket;
        this.owner = owner;
        this.iceSocket = iceSocket;
        if (transport == TransportType.TCP) {
            this.socketType = socket.getTcpSocketType();
        } else {
            this.socketType = null;
        }
    }

    public LocalCandidate(IcePeer owner, IceSocket iceSocket, CandidateType type, InetAddress address, int port, LocalCandidate base) {
        this.type = type;
        this.address = address;
        this.port = port;
        this.base = base;
        this.componentId = base.componentId;
        this.transport = base.socket.getTransportType();
        this.socket = base.socket;
        this.owner = owner;
        this.iceSocket = iceSocket;
        if (transport == TransportType.TCP) {
            this.socketType = socket.getTcpSocketType();
        } else {
            this.socketType = null;
        }
    }

    public IceSocket getIceSocket() {
        return iceSocket;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[type=" + getType() + ":address=" + getAddress() + ":port=" + getPort() + ":priority=" + getPriority() + ":componentId=" + getComponentId() + ((getBase() != this) ? ":base=" + getBase() : ":base=this") + ":socket=" + socket + "]";
    }

    @Override
    protected void setFoundation(String foundation) {
        // Does nothing
    }

    @Override
    protected void setBaseAddress(InetAddress baseAddress) {
        throw new UnsupportedOperationException("Cannot set the base address");
    }

    @Override
    protected void setBasePort(int basePort) {
        throw new UnsupportedOperationException("Cannot set the base port");
    }

    @Override
    protected InetAddress getBaseAddress() {
        return getBase().getAddress();
    }

    @Override
    protected int getBasePort() {
        return getBase().getPort();
    }

    /**
     * @return the owner
     */
    public IcePeer getOwner() {
        return owner;
    }


    @Override
    public InetAddress getAddress() {
        return address;
    }

    @Override
    public short getComponentId() {
        return componentId;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public long getPriority() {
        return priority;
    }

    @Override
    public TransportType getTransport() {
        return transport;
    }

    @Override
    public CandidateType getType() {
        return type;
    }

    @Override
    public void setPriority(long priority) {
        this.priority = priority;
    }
}
