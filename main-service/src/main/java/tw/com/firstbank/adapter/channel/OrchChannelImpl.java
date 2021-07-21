package tw.com.firstbank.adapter.channel;

public class OrchChannelImpl implements OrchChannel {

  @Override
  public void listen(String in) {
    System.out.println("Message read from Queue : " + in);
  }

}
