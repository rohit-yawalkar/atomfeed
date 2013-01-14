package org.ict4htw.atomfeed.server.repository;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ict4htw.atomfeed.SpringIntegrationIT;
import org.ict4htw.atomfeed.server.domain.EventRecord;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AllEventRecordsIT extends SpringIntegrationIT {

    @Autowired
    private AllEventRecords allEventRecords;

    @Before
    @After
    public void purgeEventRecords() {
        template.deleteAll(template.loadAll(EventRecord.class));
    }

    @Test
    public void shouldAddEventRecordAndFetchByUUID() throws URISyntaxException {
        String uuid = UUID.randomUUID().toString();

        EventRecord eventRecordAdded = new EventRecord(uuid, "title", new URI("http://uri"), null);

        allEventRecords.add(eventRecordAdded);

        EventRecord eventRecordFetched = allEventRecords.get(uuid);

        assertEquals(eventRecordAdded.getUuid(), eventRecordFetched.getUuid());
        assertEquals(eventRecordAdded.getTitle(), eventRecordFetched.getTitle());
        assertTrue((new Date()).after(eventRecordFetched.getTimeStamp()));
    }

    @Test
    public void shouldGetTotalCountOfEventRecords() throws URISyntaxException {
        EventRecord eventRecordAdded1 = new EventRecord("uuid1", "title", new URI("http://uri"), null);
        EventRecord eventRecordAdded2 = new EventRecord("uuid2", "title", new URI("http://uri"), null);

        allEventRecords.add(eventRecordAdded1);
        allEventRecords.add(eventRecordAdded2);

        int totalCount = allEventRecords.getTotalCount();

        Assert.assertEquals(2, totalCount);
    }

    @Test
    public void shouldGetEventsFromStartNumber() throws URISyntaxException {
        EventRecord eventRecordAdded1 = new EventRecord("uuid1", "title", new URI("http://uri"), null);
        EventRecord eventRecordAdded2 = new EventRecord("uuid2", "title", new URI("http://uri"), null);
        EventRecord eventRecordAdded3 = new EventRecord("uuid3", "title", new URI("http://uri"), null);
        EventRecord eventRecordAdded4 = new EventRecord("uuid4", "title", new URI("http://uri"), null);
        EventRecord eventRecordAdded5 = new EventRecord("uuid5", "title", new URI("http://uri"), null);

        allEventRecords.add(eventRecordAdded1);
        allEventRecords.add(eventRecordAdded2);
        allEventRecords.add(eventRecordAdded3);
        allEventRecords.add(eventRecordAdded4);
        allEventRecords.add(eventRecordAdded5);

        List<EventRecord> eventRecordList = allEventRecords.getEventsFromNumber(3, 2);

        Assert.assertEquals(2, eventRecordList.size());
        assertEquals(eventRecordAdded4.getUuid(), eventRecordList.get(0).getUuid());
        assertEquals(eventRecordAdded5.getUuid(), eventRecordList.get(1).getUuid());
    }

}
