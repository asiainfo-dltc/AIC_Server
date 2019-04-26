package com.asiainfo.model;


public class KafkaLagHisEnity {

  private String groupId;
  private String topic;
  private String partition;
  private String currentOffset;
  private String logEndOffset;
  private String lag;
  private String operationTime;


  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }


  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }


  public String getPartition() {
    return partition;
  }

  public void setPartition(String partiotion) {
    this.partition = partiotion;
  }


  public String getCurrentOffset() {
    return currentOffset;
  }

  public void setCurrentOffset(String currentOffset) {
    this.currentOffset = currentOffset;
  }


  public String getLogEndOffset() {
    return logEndOffset;
  }

  public void setLogEndOffset(String logEndOffset) {
    this.logEndOffset = logEndOffset;
  }


  public String getLag() {
    return lag;
  }

  public void setLag(String lag) {
    this.lag = lag;
  }


  public String getOperationTime() {
    return operationTime;
  }

  public void setOperationTime(String operationTime) {
    this.operationTime = operationTime;
  }

}
