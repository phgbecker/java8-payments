package util;

import entity.Subscription;

import java.text.NumberFormat;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Consumer;

public class TotalPaidSubscription implements Consumer<Subscription> {

    private ChronoUnit chronoUnit;

    public TotalPaidSubscription(ChronoUnit chronoUnit) {
        this.chronoUnit = chronoUnit;
    }

    private Optional<ChronoUnit> getChronoUnit() {
        return Optional.ofNullable(chronoUnit);
    }

    @Override
    public void accept(Subscription subscription) {
        System.out.format("Customer \"%s\" paid %s in subscription over the last %s\n",
                subscription.getCustomer(),
                NumberFormat.getCurrencyInstance().format(subscription.getTotalPaid(getChronoUnit().orElse(ChronoUnit.MONTHS))),
                subscription.getTimeElapsed(getChronoUnit().orElse(ChronoUnit.MONTHS)) + " " + getChronoUnit().orElse(ChronoUnit.MONTHS).toString().toLowerCase()
        );
    }

}
