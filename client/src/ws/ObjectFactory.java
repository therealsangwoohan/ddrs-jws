
package ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CreateRoomResponse_QNAME = new QName("http://ws/", "createRoomResponse");
    private final static QName _DeleteRoom_QNAME = new QName("http://ws/", "deleteRoom");
    private final static QName _CreateRoom_QNAME = new QName("http://ws/", "createRoom");
    private final static QName _DeleteRoomResponse_QNAME = new QName("http://ws/", "deleteRoomResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreateRoomResponse }
     * 
     */
    public CreateRoomResponse createCreateRoomResponse() {
        return new CreateRoomResponse();
    }

    /**
     * Create an instance of {@link DeleteRoom }
     * 
     */
    public DeleteRoom createDeleteRoom() {
        return new DeleteRoom();
    }

    /**
     * Create an instance of {@link CreateRoom }
     * 
     */
    public CreateRoom createCreateRoom() {
        return new CreateRoom();
    }

    /**
     * Create an instance of {@link DeleteRoomResponse }
     * 
     */
    public DeleteRoomResponse createDeleteRoomResponse() {
        return new DeleteRoomResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateRoomResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "createRoomResponse")
    public JAXBElement<CreateRoomResponse> createCreateRoomResponse(CreateRoomResponse value) {
        return new JAXBElement<CreateRoomResponse>(_CreateRoomResponse_QNAME, CreateRoomResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRoom }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "deleteRoom")
    public JAXBElement<DeleteRoom> createDeleteRoom(DeleteRoom value) {
        return new JAXBElement<DeleteRoom>(_DeleteRoom_QNAME, DeleteRoom.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateRoom }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "createRoom")
    public JAXBElement<CreateRoom> createCreateRoom(CreateRoom value) {
        return new JAXBElement<CreateRoom>(_CreateRoom_QNAME, CreateRoom.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRoomResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "deleteRoomResponse")
    public JAXBElement<DeleteRoomResponse> createDeleteRoomResponse(DeleteRoomResponse value) {
        return new JAXBElement<DeleteRoomResponse>(_DeleteRoomResponse_QNAME, DeleteRoomResponse.class, null, value);
    }

}
