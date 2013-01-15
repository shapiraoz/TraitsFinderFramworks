package Core.Interfaces;

import Core.ECrawlingType;

public interface ICrawlerProcessor
{
	boolean LoadProcessor(String processorFilePath);
	boolean GetDepthCrawling(String className);
	boolean GetDepthCrawling(ECrawlingType crawlType);
}
