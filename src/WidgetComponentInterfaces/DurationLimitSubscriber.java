package WidgetComponentInterfaces;

import WidgetComponents.DurationLimiter.Mode;

public interface DurationLimitSubscriber 
{
	public void notifyDurationLimit(int hour, int minute, Mode m);
}
