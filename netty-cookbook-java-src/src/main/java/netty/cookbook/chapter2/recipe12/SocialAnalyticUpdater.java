package netty.cookbook.chapter2.recipe12;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SocialAnalyticUpdater implements PropertyChangeListener {
  public SocialAnalyticUpdater(WebUrl model) {
    model.addChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    System.out.println("Changed property: " + event.getPropertyName() + " [old -> "
      + event.getOldValue() + "] | [new -> " + event.getNewValue() +"]");
  }
} 