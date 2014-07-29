package com.jivesoftware.jive.hbasewaltest;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

/**
 *
 */
public class EventProvider {

    private final ObjectMapper mapper = new ObjectMapper();
    private final String eventString = "{\n"
        + "  \"CommentVersion\" : {\n"
        + "    \"instanceId\" : \"andsq56bizavy Views\",\n"
        + "    \"processedBody\" : \"\n"
        + "A comment on a document.\n"
        + "\n"
        + "\",\n"
        + "    \"truncatedBody\" : \"\n"
        + "A comment on a document.\n"
        + "\n"
        + "\",\n"
        + "    \"userMentions\" : [ ],\n"
        + "    \"containerMentions\" : [ ],\n"
        + "    \"tags\" : [ ],\n"
        + "    \"author\" : \"User_kseekcnfi7ewy Events Views\",\n"
        + "    \"rawSubject\" : \"\n"
        + "A comment on a document.\n"
        + "\n"
        + "\",\n"
        + "    \"rawBody\" : \"\n"
        + "A comment on a document.\n"
        + "\n"
        + "\",\n"
        + "    \"language\" : \"en\",\n"
        + "    \"creationDate\" : \"2014-07-22T17:13:08.001Z\",\n"
        + "    \"versionParent\" : \"Comment_andsq56bizave Events Views\",\n"
        + "    \"inheritPermissionsFrom\" : \"Comment_andsq56bizave Events Views\",\n"
        + "    \"activityTarget\" : \"Comment_andsq56bizave Events Views\",\n"
        + "    \"activityParent\" : \"Document_andsq56bizae4 Events Views\",\n"
        + "    \"activityVerb\" : \"CREATED\"\n"
        + "  },\n"
        + "  \"tenantId\" : \"ace8\",\n"
        + "  \"actorId\" : \"kseekcnfi7ewy Views\",\n"
        + "  \"userId\" : \"kseekcnfi7ewy Views\",\n"
        + "  \"activityVerb\" : \"CREATED\",\n"
        + "  \"trackEventProcessedLifecycle\" : true,\n"
        + "  \"modelVersionId\" : \"1406030203071:1406045924832\",\n"
        + "  \"trace\" : \"4f52a4992040dc5b:a42e9205fea0b6d9:358a9fce11d3e3ae\",\n"
        + "  \"eventId\" : \"236202159665225728\"\n"
        + "}";
    private final ObjectNode event;

    public EventProvider() throws IOException {
        event = mapper.readValue(eventString, ObjectNode.class);
    }

    ObjectNode provideEvent() {
        return event;
    }
}
