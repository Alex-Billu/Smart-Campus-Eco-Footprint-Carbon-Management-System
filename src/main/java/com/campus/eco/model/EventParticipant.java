package com.campus.eco.model;

public class EventParticipant {
    private int participantId;
    private int eventId;
    private Integer deptId;
    private String participantName;
    private boolean attended;
    private int pointsAwarded;

    public EventParticipant() {}

    public int getParticipantId() { return participantId; }
    public void setParticipantId(int participantId) { this.participantId = participantId; }
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getParticipantName() { return participantName; }
    public void setParticipantName(String participantName) { this.participantName = participantName; }
    public boolean isAttended() { return attended; }
    public void setAttended(boolean attended) { this.attended = attended; }
    public int getPointsAwarded() { return pointsAwarded; }
    public void setPointsAwarded(int pointsAwarded) { this.pointsAwarded = pointsAwarded; }
}
