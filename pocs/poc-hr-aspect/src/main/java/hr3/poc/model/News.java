package hr3.poc.model;

public interface News extends Action {

	java.lang.Long getActionId();

	void setActionId(java.lang.Long actionId);

	java.lang.String getNewsContent();

	void setNewsContent(java.lang.String newsContent);

	java.lang.String getNewsMetadata();

	void setNewsMetadata(java.lang.String newsMetadata);

}