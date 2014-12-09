package netty.cookbook.chapter4.recipe5;

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