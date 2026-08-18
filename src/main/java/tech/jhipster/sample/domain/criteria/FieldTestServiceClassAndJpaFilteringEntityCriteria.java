package tech.jhipster.sample.domain.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.sample.domain.enumeration.EnumFieldClass;
import tech.jhipster.sample.domain.enumeration.EnumRequiredFieldClass;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link tech.jhipster.sample.domain.FieldTestServiceClassAndJpaFilteringEntity} entity. This class is used
 * in {@link tech.jhipster.sample.web.rest.FieldTestServiceClassAndJpaFilteringEntityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /field-test-service-class-and-jpa-filtering-entities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FieldTestServiceClassAndJpaFilteringEntityCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LocalTime
     */
    public static class LocalTimeFilter extends RangeFilter<LocalTime> {

        public LocalTimeFilter() {}

        public LocalTimeFilter(LocalTimeFilter filter) {
            super(filter);
        }

        @Override
        public LocalTimeFilter copy() {
            return new LocalTimeFilter(this);
        }
    }

    /**
     * Class for filtering EnumFieldClass
     */
    public static class EnumFieldClassFilter extends Filter<EnumFieldClass> {

        public EnumFieldClassFilter() {}

        public EnumFieldClassFilter(EnumFieldClassFilter filter) {
            super(filter);
        }

        @Override
        public EnumFieldClassFilter copy() {
            return new EnumFieldClassFilter(this);
        }
    }

    /**
     * Class for filtering EnumRequiredFieldClass
     */
    public static class EnumRequiredFieldClassFilter extends Filter<EnumRequiredFieldClass> {

        public EnumRequiredFieldClassFilter() {}

        public EnumRequiredFieldClassFilter(EnumRequiredFieldClassFilter filter) {
            super(filter);
        }

        @Override
        public EnumRequiredFieldClassFilter copy() {
            return new EnumRequiredFieldClassFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter stringBob;

    private StringFilter stringRequiredBob;

    private StringFilter stringMinlengthBob;

    private StringFilter stringMaxlengthBob;

    private StringFilter stringPatternBob;

    private IntegerFilter integerBob;

    private IntegerFilter integerRequiredBob;

    private IntegerFilter integerMinBob;

    private IntegerFilter integerMaxBob;

    private LongFilter longBob;

    private LongFilter longRequiredBob;

    private LongFilter longMinBob;

    private LongFilter longMaxBob;

    private FloatFilter floatBob;

    private FloatFilter floatRequiredBob;

    private FloatFilter floatMinBob;

    private FloatFilter floatMaxBob;

    private DoubleFilter doubleRequiredBob;

    private DoubleFilter doubleMinBob;

    private DoubleFilter doubleMaxBob;

    private BigDecimalFilter bigDecimalRequiredBob;

    private BigDecimalFilter bigDecimalMinBob;

    private BigDecimalFilter bigDecimalMaxBob;

    private LocalDateFilter localDateBob;

    private LocalDateFilter localDateRequiredBob;

    private InstantFilter instantBob;

    private InstantFilter instanteRequiredBob;

    private ZonedDateTimeFilter zonedDateTimeBob;

    private ZonedDateTimeFilter zonedDateTimeRequiredBob;

    private LocalTimeFilter localTimeBob;

    private LocalTimeFilter localTimeRequiredBob;

    private DurationFilter durationBob;

    private DurationFilter durationRequiredBob;

    private BooleanFilter booleanBob;

    private BooleanFilter booleanRequiredBob;

    private EnumFieldClassFilter enumBob;

    private EnumRequiredFieldClassFilter enumRequiredBob;

    private UUIDFilter uuidBob;

    private UUIDFilter uuidRequiredBob;

    private Boolean distinct;

    public FieldTestServiceClassAndJpaFilteringEntityCriteria() {}

    public FieldTestServiceClassAndJpaFilteringEntityCriteria(FieldTestServiceClassAndJpaFilteringEntityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.stringBob = other.optionalStringBob().map(StringFilter::copy).orElse(null);
        this.stringRequiredBob = other.optionalStringRequiredBob().map(StringFilter::copy).orElse(null);
        this.stringMinlengthBob = other.optionalStringMinlengthBob().map(StringFilter::copy).orElse(null);
        this.stringMaxlengthBob = other.optionalStringMaxlengthBob().map(StringFilter::copy).orElse(null);
        this.stringPatternBob = other.optionalStringPatternBob().map(StringFilter::copy).orElse(null);
        this.integerBob = other.optionalIntegerBob().map(IntegerFilter::copy).orElse(null);
        this.integerRequiredBob = other.optionalIntegerRequiredBob().map(IntegerFilter::copy).orElse(null);
        this.integerMinBob = other.optionalIntegerMinBob().map(IntegerFilter::copy).orElse(null);
        this.integerMaxBob = other.optionalIntegerMaxBob().map(IntegerFilter::copy).orElse(null);
        this.longBob = other.optionalLongBob().map(LongFilter::copy).orElse(null);
        this.longRequiredBob = other.optionalLongRequiredBob().map(LongFilter::copy).orElse(null);
        this.longMinBob = other.optionalLongMinBob().map(LongFilter::copy).orElse(null);
        this.longMaxBob = other.optionalLongMaxBob().map(LongFilter::copy).orElse(null);
        this.floatBob = other.optionalFloatBob().map(FloatFilter::copy).orElse(null);
        this.floatRequiredBob = other.optionalFloatRequiredBob().map(FloatFilter::copy).orElse(null);
        this.floatMinBob = other.optionalFloatMinBob().map(FloatFilter::copy).orElse(null);
        this.floatMaxBob = other.optionalFloatMaxBob().map(FloatFilter::copy).orElse(null);
        this.doubleRequiredBob = other.optionalDoubleRequiredBob().map(DoubleFilter::copy).orElse(null);
        this.doubleMinBob = other.optionalDoubleMinBob().map(DoubleFilter::copy).orElse(null);
        this.doubleMaxBob = other.optionalDoubleMaxBob().map(DoubleFilter::copy).orElse(null);
        this.bigDecimalRequiredBob = other.optionalBigDecimalRequiredBob().map(BigDecimalFilter::copy).orElse(null);
        this.bigDecimalMinBob = other.optionalBigDecimalMinBob().map(BigDecimalFilter::copy).orElse(null);
        this.bigDecimalMaxBob = other.optionalBigDecimalMaxBob().map(BigDecimalFilter::copy).orElse(null);
        this.localDateBob = other.optionalLocalDateBob().map(LocalDateFilter::copy).orElse(null);
        this.localDateRequiredBob = other.optionalLocalDateRequiredBob().map(LocalDateFilter::copy).orElse(null);
        this.instantBob = other.optionalInstantBob().map(InstantFilter::copy).orElse(null);
        this.instanteRequiredBob = other.optionalInstanteRequiredBob().map(InstantFilter::copy).orElse(null);
        this.zonedDateTimeBob = other.optionalZonedDateTimeBob().map(ZonedDateTimeFilter::copy).orElse(null);
        this.zonedDateTimeRequiredBob = other.optionalZonedDateTimeRequiredBob().map(ZonedDateTimeFilter::copy).orElse(null);
        this.localTimeBob = other.optionalLocalTimeBob().map(LocalTimeFilter::copy).orElse(null);
        this.localTimeRequiredBob = other.optionalLocalTimeRequiredBob().map(LocalTimeFilter::copy).orElse(null);
        this.durationBob = other.optionalDurationBob().map(DurationFilter::copy).orElse(null);
        this.durationRequiredBob = other.optionalDurationRequiredBob().map(DurationFilter::copy).orElse(null);
        this.booleanBob = other.optionalBooleanBob().map(BooleanFilter::copy).orElse(null);
        this.booleanRequiredBob = other.optionalBooleanRequiredBob().map(BooleanFilter::copy).orElse(null);
        this.enumBob = other.optionalEnumBob().map(EnumFieldClassFilter::copy).orElse(null);
        this.enumRequiredBob = other.optionalEnumRequiredBob().map(EnumRequiredFieldClassFilter::copy).orElse(null);
        this.uuidBob = other.optionalUuidBob().map(UUIDFilter::copy).orElse(null);
        this.uuidRequiredBob = other.optionalUuidRequiredBob().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FieldTestServiceClassAndJpaFilteringEntityCriteria copy() {
        return new FieldTestServiceClassAndJpaFilteringEntityCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStringBob() {
        return stringBob;
    }

    public Optional<StringFilter> optionalStringBob() {
        return Optional.ofNullable(stringBob);
    }

    public StringFilter stringBob() {
        if (stringBob == null) {
            setStringBob(new StringFilter());
        }
        return stringBob;
    }

    public void setStringBob(StringFilter stringBob) {
        this.stringBob = stringBob;
    }

    public StringFilter getStringRequiredBob() {
        return stringRequiredBob;
    }

    public Optional<StringFilter> optionalStringRequiredBob() {
        return Optional.ofNullable(stringRequiredBob);
    }

    public StringFilter stringRequiredBob() {
        if (stringRequiredBob == null) {
            setStringRequiredBob(new StringFilter());
        }
        return stringRequiredBob;
    }

    public void setStringRequiredBob(StringFilter stringRequiredBob) {
        this.stringRequiredBob = stringRequiredBob;
    }

    public StringFilter getStringMinlengthBob() {
        return stringMinlengthBob;
    }

    public Optional<StringFilter> optionalStringMinlengthBob() {
        return Optional.ofNullable(stringMinlengthBob);
    }

    public StringFilter stringMinlengthBob() {
        if (stringMinlengthBob == null) {
            setStringMinlengthBob(new StringFilter());
        }
        return stringMinlengthBob;
    }

    public void setStringMinlengthBob(StringFilter stringMinlengthBob) {
        this.stringMinlengthBob = stringMinlengthBob;
    }

    public StringFilter getStringMaxlengthBob() {
        return stringMaxlengthBob;
    }

    public Optional<StringFilter> optionalStringMaxlengthBob() {
        return Optional.ofNullable(stringMaxlengthBob);
    }

    public StringFilter stringMaxlengthBob() {
        if (stringMaxlengthBob == null) {
            setStringMaxlengthBob(new StringFilter());
        }
        return stringMaxlengthBob;
    }

    public void setStringMaxlengthBob(StringFilter stringMaxlengthBob) {
        this.stringMaxlengthBob = stringMaxlengthBob;
    }

    public StringFilter getStringPatternBob() {
        return stringPatternBob;
    }

    public Optional<StringFilter> optionalStringPatternBob() {
        return Optional.ofNullable(stringPatternBob);
    }

    public StringFilter stringPatternBob() {
        if (stringPatternBob == null) {
            setStringPatternBob(new StringFilter());
        }
        return stringPatternBob;
    }

    public void setStringPatternBob(StringFilter stringPatternBob) {
        this.stringPatternBob = stringPatternBob;
    }

    public IntegerFilter getIntegerBob() {
        return integerBob;
    }

    public Optional<IntegerFilter> optionalIntegerBob() {
        return Optional.ofNullable(integerBob);
    }

    public IntegerFilter integerBob() {
        if (integerBob == null) {
            setIntegerBob(new IntegerFilter());
        }
        return integerBob;
    }

    public void setIntegerBob(IntegerFilter integerBob) {
        this.integerBob = integerBob;
    }

    public IntegerFilter getIntegerRequiredBob() {
        return integerRequiredBob;
    }

    public Optional<IntegerFilter> optionalIntegerRequiredBob() {
        return Optional.ofNullable(integerRequiredBob);
    }

    public IntegerFilter integerRequiredBob() {
        if (integerRequiredBob == null) {
            setIntegerRequiredBob(new IntegerFilter());
        }
        return integerRequiredBob;
    }

    public void setIntegerRequiredBob(IntegerFilter integerRequiredBob) {
        this.integerRequiredBob = integerRequiredBob;
    }

    public IntegerFilter getIntegerMinBob() {
        return integerMinBob;
    }

    public Optional<IntegerFilter> optionalIntegerMinBob() {
        return Optional.ofNullable(integerMinBob);
    }

    public IntegerFilter integerMinBob() {
        if (integerMinBob == null) {
            setIntegerMinBob(new IntegerFilter());
        }
        return integerMinBob;
    }

    public void setIntegerMinBob(IntegerFilter integerMinBob) {
        this.integerMinBob = integerMinBob;
    }

    public IntegerFilter getIntegerMaxBob() {
        return integerMaxBob;
    }

    public Optional<IntegerFilter> optionalIntegerMaxBob() {
        return Optional.ofNullable(integerMaxBob);
    }

    public IntegerFilter integerMaxBob() {
        if (integerMaxBob == null) {
            setIntegerMaxBob(new IntegerFilter());
        }
        return integerMaxBob;
    }

    public void setIntegerMaxBob(IntegerFilter integerMaxBob) {
        this.integerMaxBob = integerMaxBob;
    }

    public LongFilter getLongBob() {
        return longBob;
    }

    public Optional<LongFilter> optionalLongBob() {
        return Optional.ofNullable(longBob);
    }

    public LongFilter longBob() {
        if (longBob == null) {
            setLongBob(new LongFilter());
        }
        return longBob;
    }

    public void setLongBob(LongFilter longBob) {
        this.longBob = longBob;
    }

    public LongFilter getLongRequiredBob() {
        return longRequiredBob;
    }

    public Optional<LongFilter> optionalLongRequiredBob() {
        return Optional.ofNullable(longRequiredBob);
    }

    public LongFilter longRequiredBob() {
        if (longRequiredBob == null) {
            setLongRequiredBob(new LongFilter());
        }
        return longRequiredBob;
    }

    public void setLongRequiredBob(LongFilter longRequiredBob) {
        this.longRequiredBob = longRequiredBob;
    }

    public LongFilter getLongMinBob() {
        return longMinBob;
    }

    public Optional<LongFilter> optionalLongMinBob() {
        return Optional.ofNullable(longMinBob);
    }

    public LongFilter longMinBob() {
        if (longMinBob == null) {
            setLongMinBob(new LongFilter());
        }
        return longMinBob;
    }

    public void setLongMinBob(LongFilter longMinBob) {
        this.longMinBob = longMinBob;
    }

    public LongFilter getLongMaxBob() {
        return longMaxBob;
    }

    public Optional<LongFilter> optionalLongMaxBob() {
        return Optional.ofNullable(longMaxBob);
    }

    public LongFilter longMaxBob() {
        if (longMaxBob == null) {
            setLongMaxBob(new LongFilter());
        }
        return longMaxBob;
    }

    public void setLongMaxBob(LongFilter longMaxBob) {
        this.longMaxBob = longMaxBob;
    }

    public FloatFilter getFloatBob() {
        return floatBob;
    }

    public Optional<FloatFilter> optionalFloatBob() {
        return Optional.ofNullable(floatBob);
    }

    public FloatFilter floatBob() {
        if (floatBob == null) {
            setFloatBob(new FloatFilter());
        }
        return floatBob;
    }

    public void setFloatBob(FloatFilter floatBob) {
        this.floatBob = floatBob;
    }

    public FloatFilter getFloatRequiredBob() {
        return floatRequiredBob;
    }

    public Optional<FloatFilter> optionalFloatRequiredBob() {
        return Optional.ofNullable(floatRequiredBob);
    }

    public FloatFilter floatRequiredBob() {
        if (floatRequiredBob == null) {
            setFloatRequiredBob(new FloatFilter());
        }
        return floatRequiredBob;
    }

    public void setFloatRequiredBob(FloatFilter floatRequiredBob) {
        this.floatRequiredBob = floatRequiredBob;
    }

    public FloatFilter getFloatMinBob() {
        return floatMinBob;
    }

    public Optional<FloatFilter> optionalFloatMinBob() {
        return Optional.ofNullable(floatMinBob);
    }

    public FloatFilter floatMinBob() {
        if (floatMinBob == null) {
            setFloatMinBob(new FloatFilter());
        }
        return floatMinBob;
    }

    public void setFloatMinBob(FloatFilter floatMinBob) {
        this.floatMinBob = floatMinBob;
    }

    public FloatFilter getFloatMaxBob() {
        return floatMaxBob;
    }

    public Optional<FloatFilter> optionalFloatMaxBob() {
        return Optional.ofNullable(floatMaxBob);
    }

    public FloatFilter floatMaxBob() {
        if (floatMaxBob == null) {
            setFloatMaxBob(new FloatFilter());
        }
        return floatMaxBob;
    }

    public void setFloatMaxBob(FloatFilter floatMaxBob) {
        this.floatMaxBob = floatMaxBob;
    }

    public DoubleFilter getDoubleRequiredBob() {
        return doubleRequiredBob;
    }

    public Optional<DoubleFilter> optionalDoubleRequiredBob() {
        return Optional.ofNullable(doubleRequiredBob);
    }

    public DoubleFilter doubleRequiredBob() {
        if (doubleRequiredBob == null) {
            setDoubleRequiredBob(new DoubleFilter());
        }
        return doubleRequiredBob;
    }

    public void setDoubleRequiredBob(DoubleFilter doubleRequiredBob) {
        this.doubleRequiredBob = doubleRequiredBob;
    }

    public DoubleFilter getDoubleMinBob() {
        return doubleMinBob;
    }

    public Optional<DoubleFilter> optionalDoubleMinBob() {
        return Optional.ofNullable(doubleMinBob);
    }

    public DoubleFilter doubleMinBob() {
        if (doubleMinBob == null) {
            setDoubleMinBob(new DoubleFilter());
        }
        return doubleMinBob;
    }

    public void setDoubleMinBob(DoubleFilter doubleMinBob) {
        this.doubleMinBob = doubleMinBob;
    }

    public DoubleFilter getDoubleMaxBob() {
        return doubleMaxBob;
    }

    public Optional<DoubleFilter> optionalDoubleMaxBob() {
        return Optional.ofNullable(doubleMaxBob);
    }

    public DoubleFilter doubleMaxBob() {
        if (doubleMaxBob == null) {
            setDoubleMaxBob(new DoubleFilter());
        }
        return doubleMaxBob;
    }

    public void setDoubleMaxBob(DoubleFilter doubleMaxBob) {
        this.doubleMaxBob = doubleMaxBob;
    }

    public BigDecimalFilter getBigDecimalRequiredBob() {
        return bigDecimalRequiredBob;
    }

    public Optional<BigDecimalFilter> optionalBigDecimalRequiredBob() {
        return Optional.ofNullable(bigDecimalRequiredBob);
    }

    public BigDecimalFilter bigDecimalRequiredBob() {
        if (bigDecimalRequiredBob == null) {
            setBigDecimalRequiredBob(new BigDecimalFilter());
        }
        return bigDecimalRequiredBob;
    }

    public void setBigDecimalRequiredBob(BigDecimalFilter bigDecimalRequiredBob) {
        this.bigDecimalRequiredBob = bigDecimalRequiredBob;
    }

    public BigDecimalFilter getBigDecimalMinBob() {
        return bigDecimalMinBob;
    }

    public Optional<BigDecimalFilter> optionalBigDecimalMinBob() {
        return Optional.ofNullable(bigDecimalMinBob);
    }

    public BigDecimalFilter bigDecimalMinBob() {
        if (bigDecimalMinBob == null) {
            setBigDecimalMinBob(new BigDecimalFilter());
        }
        return bigDecimalMinBob;
    }

    public void setBigDecimalMinBob(BigDecimalFilter bigDecimalMinBob) {
        this.bigDecimalMinBob = bigDecimalMinBob;
    }

    public BigDecimalFilter getBigDecimalMaxBob() {
        return bigDecimalMaxBob;
    }

    public Optional<BigDecimalFilter> optionalBigDecimalMaxBob() {
        return Optional.ofNullable(bigDecimalMaxBob);
    }

    public BigDecimalFilter bigDecimalMaxBob() {
        if (bigDecimalMaxBob == null) {
            setBigDecimalMaxBob(new BigDecimalFilter());
        }
        return bigDecimalMaxBob;
    }

    public void setBigDecimalMaxBob(BigDecimalFilter bigDecimalMaxBob) {
        this.bigDecimalMaxBob = bigDecimalMaxBob;
    }

    public LocalDateFilter getLocalDateBob() {
        return localDateBob;
    }

    public Optional<LocalDateFilter> optionalLocalDateBob() {
        return Optional.ofNullable(localDateBob);
    }

    public LocalDateFilter localDateBob() {
        if (localDateBob == null) {
            setLocalDateBob(new LocalDateFilter());
        }
        return localDateBob;
    }

    public void setLocalDateBob(LocalDateFilter localDateBob) {
        this.localDateBob = localDateBob;
    }

    public LocalDateFilter getLocalDateRequiredBob() {
        return localDateRequiredBob;
    }

    public Optional<LocalDateFilter> optionalLocalDateRequiredBob() {
        return Optional.ofNullable(localDateRequiredBob);
    }

    public LocalDateFilter localDateRequiredBob() {
        if (localDateRequiredBob == null) {
            setLocalDateRequiredBob(new LocalDateFilter());
        }
        return localDateRequiredBob;
    }

    public void setLocalDateRequiredBob(LocalDateFilter localDateRequiredBob) {
        this.localDateRequiredBob = localDateRequiredBob;
    }

    public InstantFilter getInstantBob() {
        return instantBob;
    }

    public Optional<InstantFilter> optionalInstantBob() {
        return Optional.ofNullable(instantBob);
    }

    public InstantFilter instantBob() {
        if (instantBob == null) {
            setInstantBob(new InstantFilter());
        }
        return instantBob;
    }

    public void setInstantBob(InstantFilter instantBob) {
        this.instantBob = instantBob;
    }

    public InstantFilter getInstanteRequiredBob() {
        return instanteRequiredBob;
    }

    public Optional<InstantFilter> optionalInstanteRequiredBob() {
        return Optional.ofNullable(instanteRequiredBob);
    }

    public InstantFilter instanteRequiredBob() {
        if (instanteRequiredBob == null) {
            setInstanteRequiredBob(new InstantFilter());
        }
        return instanteRequiredBob;
    }

    public void setInstanteRequiredBob(InstantFilter instanteRequiredBob) {
        this.instanteRequiredBob = instanteRequiredBob;
    }

    public ZonedDateTimeFilter getZonedDateTimeBob() {
        return zonedDateTimeBob;
    }

    public Optional<ZonedDateTimeFilter> optionalZonedDateTimeBob() {
        return Optional.ofNullable(zonedDateTimeBob);
    }

    public ZonedDateTimeFilter zonedDateTimeBob() {
        if (zonedDateTimeBob == null) {
            setZonedDateTimeBob(new ZonedDateTimeFilter());
        }
        return zonedDateTimeBob;
    }

    public void setZonedDateTimeBob(ZonedDateTimeFilter zonedDateTimeBob) {
        this.zonedDateTimeBob = zonedDateTimeBob;
    }

    public ZonedDateTimeFilter getZonedDateTimeRequiredBob() {
        return zonedDateTimeRequiredBob;
    }

    public Optional<ZonedDateTimeFilter> optionalZonedDateTimeRequiredBob() {
        return Optional.ofNullable(zonedDateTimeRequiredBob);
    }

    public ZonedDateTimeFilter zonedDateTimeRequiredBob() {
        if (zonedDateTimeRequiredBob == null) {
            setZonedDateTimeRequiredBob(new ZonedDateTimeFilter());
        }
        return zonedDateTimeRequiredBob;
    }

    public void setZonedDateTimeRequiredBob(ZonedDateTimeFilter zonedDateTimeRequiredBob) {
        this.zonedDateTimeRequiredBob = zonedDateTimeRequiredBob;
    }

    public LocalTimeFilter getLocalTimeBob() {
        return localTimeBob;
    }

    public Optional<LocalTimeFilter> optionalLocalTimeBob() {
        return Optional.ofNullable(localTimeBob);
    }

    public LocalTimeFilter localTimeBob() {
        if (localTimeBob == null) {
            setLocalTimeBob(new LocalTimeFilter());
        }
        return localTimeBob;
    }

    public void setLocalTimeBob(LocalTimeFilter localTimeBob) {
        this.localTimeBob = localTimeBob;
    }

    public LocalTimeFilter getLocalTimeRequiredBob() {
        return localTimeRequiredBob;
    }

    public Optional<LocalTimeFilter> optionalLocalTimeRequiredBob() {
        return Optional.ofNullable(localTimeRequiredBob);
    }

    public LocalTimeFilter localTimeRequiredBob() {
        if (localTimeRequiredBob == null) {
            setLocalTimeRequiredBob(new LocalTimeFilter());
        }
        return localTimeRequiredBob;
    }

    public void setLocalTimeRequiredBob(LocalTimeFilter localTimeRequiredBob) {
        this.localTimeRequiredBob = localTimeRequiredBob;
    }

    public DurationFilter getDurationBob() {
        return durationBob;
    }

    public Optional<DurationFilter> optionalDurationBob() {
        return Optional.ofNullable(durationBob);
    }

    public DurationFilter durationBob() {
        if (durationBob == null) {
            setDurationBob(new DurationFilter());
        }
        return durationBob;
    }

    public void setDurationBob(DurationFilter durationBob) {
        this.durationBob = durationBob;
    }

    public DurationFilter getDurationRequiredBob() {
        return durationRequiredBob;
    }

    public Optional<DurationFilter> optionalDurationRequiredBob() {
        return Optional.ofNullable(durationRequiredBob);
    }

    public DurationFilter durationRequiredBob() {
        if (durationRequiredBob == null) {
            setDurationRequiredBob(new DurationFilter());
        }
        return durationRequiredBob;
    }

    public void setDurationRequiredBob(DurationFilter durationRequiredBob) {
        this.durationRequiredBob = durationRequiredBob;
    }

    public BooleanFilter getBooleanBob() {
        return booleanBob;
    }

    public Optional<BooleanFilter> optionalBooleanBob() {
        return Optional.ofNullable(booleanBob);
    }

    public BooleanFilter booleanBob() {
        if (booleanBob == null) {
            setBooleanBob(new BooleanFilter());
        }
        return booleanBob;
    }

    public void setBooleanBob(BooleanFilter booleanBob) {
        this.booleanBob = booleanBob;
    }

    public BooleanFilter getBooleanRequiredBob() {
        return booleanRequiredBob;
    }

    public Optional<BooleanFilter> optionalBooleanRequiredBob() {
        return Optional.ofNullable(booleanRequiredBob);
    }

    public BooleanFilter booleanRequiredBob() {
        if (booleanRequiredBob == null) {
            setBooleanRequiredBob(new BooleanFilter());
        }
        return booleanRequiredBob;
    }

    public void setBooleanRequiredBob(BooleanFilter booleanRequiredBob) {
        this.booleanRequiredBob = booleanRequiredBob;
    }

    public EnumFieldClassFilter getEnumBob() {
        return enumBob;
    }

    public Optional<EnumFieldClassFilter> optionalEnumBob() {
        return Optional.ofNullable(enumBob);
    }

    public EnumFieldClassFilter enumBob() {
        if (enumBob == null) {
            setEnumBob(new EnumFieldClassFilter());
        }
        return enumBob;
    }

    public void setEnumBob(EnumFieldClassFilter enumBob) {
        this.enumBob = enumBob;
    }

    public EnumRequiredFieldClassFilter getEnumRequiredBob() {
        return enumRequiredBob;
    }

    public Optional<EnumRequiredFieldClassFilter> optionalEnumRequiredBob() {
        return Optional.ofNullable(enumRequiredBob);
    }

    public EnumRequiredFieldClassFilter enumRequiredBob() {
        if (enumRequiredBob == null) {
            setEnumRequiredBob(new EnumRequiredFieldClassFilter());
        }
        return enumRequiredBob;
    }

    public void setEnumRequiredBob(EnumRequiredFieldClassFilter enumRequiredBob) {
        this.enumRequiredBob = enumRequiredBob;
    }

    public UUIDFilter getUuidBob() {
        return uuidBob;
    }

    public Optional<UUIDFilter> optionalUuidBob() {
        return Optional.ofNullable(uuidBob);
    }

    public UUIDFilter uuidBob() {
        if (uuidBob == null) {
            setUuidBob(new UUIDFilter());
        }
        return uuidBob;
    }

    public void setUuidBob(UUIDFilter uuidBob) {
        this.uuidBob = uuidBob;
    }

    public UUIDFilter getUuidRequiredBob() {
        return uuidRequiredBob;
    }

    public Optional<UUIDFilter> optionalUuidRequiredBob() {
        return Optional.ofNullable(uuidRequiredBob);
    }

    public UUIDFilter uuidRequiredBob() {
        if (uuidRequiredBob == null) {
            setUuidRequiredBob(new UUIDFilter());
        }
        return uuidRequiredBob;
    }

    public void setUuidRequiredBob(UUIDFilter uuidRequiredBob) {
        this.uuidRequiredBob = uuidRequiredBob;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FieldTestServiceClassAndJpaFilteringEntityCriteria that = (FieldTestServiceClassAndJpaFilteringEntityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(stringBob, that.stringBob) &&
            Objects.equals(stringRequiredBob, that.stringRequiredBob) &&
            Objects.equals(stringMinlengthBob, that.stringMinlengthBob) &&
            Objects.equals(stringMaxlengthBob, that.stringMaxlengthBob) &&
            Objects.equals(stringPatternBob, that.stringPatternBob) &&
            Objects.equals(integerBob, that.integerBob) &&
            Objects.equals(integerRequiredBob, that.integerRequiredBob) &&
            Objects.equals(integerMinBob, that.integerMinBob) &&
            Objects.equals(integerMaxBob, that.integerMaxBob) &&
            Objects.equals(longBob, that.longBob) &&
            Objects.equals(longRequiredBob, that.longRequiredBob) &&
            Objects.equals(longMinBob, that.longMinBob) &&
            Objects.equals(longMaxBob, that.longMaxBob) &&
            Objects.equals(floatBob, that.floatBob) &&
            Objects.equals(floatRequiredBob, that.floatRequiredBob) &&
            Objects.equals(floatMinBob, that.floatMinBob) &&
            Objects.equals(floatMaxBob, that.floatMaxBob) &&
            Objects.equals(doubleRequiredBob, that.doubleRequiredBob) &&
            Objects.equals(doubleMinBob, that.doubleMinBob) &&
            Objects.equals(doubleMaxBob, that.doubleMaxBob) &&
            Objects.equals(bigDecimalRequiredBob, that.bigDecimalRequiredBob) &&
            Objects.equals(bigDecimalMinBob, that.bigDecimalMinBob) &&
            Objects.equals(bigDecimalMaxBob, that.bigDecimalMaxBob) &&
            Objects.equals(localDateBob, that.localDateBob) &&
            Objects.equals(localDateRequiredBob, that.localDateRequiredBob) &&
            Objects.equals(instantBob, that.instantBob) &&
            Objects.equals(instanteRequiredBob, that.instanteRequiredBob) &&
            Objects.equals(zonedDateTimeBob, that.zonedDateTimeBob) &&
            Objects.equals(zonedDateTimeRequiredBob, that.zonedDateTimeRequiredBob) &&
            Objects.equals(localTimeBob, that.localTimeBob) &&
            Objects.equals(localTimeRequiredBob, that.localTimeRequiredBob) &&
            Objects.equals(durationBob, that.durationBob) &&
            Objects.equals(durationRequiredBob, that.durationRequiredBob) &&
            Objects.equals(booleanBob, that.booleanBob) &&
            Objects.equals(booleanRequiredBob, that.booleanRequiredBob) &&
            Objects.equals(enumBob, that.enumBob) &&
            Objects.equals(enumRequiredBob, that.enumRequiredBob) &&
            Objects.equals(uuidBob, that.uuidBob) &&
            Objects.equals(uuidRequiredBob, that.uuidRequiredBob) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            stringBob,
            stringRequiredBob,
            stringMinlengthBob,
            stringMaxlengthBob,
            stringPatternBob,
            integerBob,
            integerRequiredBob,
            integerMinBob,
            integerMaxBob,
            longBob,
            longRequiredBob,
            longMinBob,
            longMaxBob,
            floatBob,
            floatRequiredBob,
            floatMinBob,
            floatMaxBob,
            doubleRequiredBob,
            doubleMinBob,
            doubleMaxBob,
            bigDecimalRequiredBob,
            bigDecimalMinBob,
            bigDecimalMaxBob,
            localDateBob,
            localDateRequiredBob,
            instantBob,
            instanteRequiredBob,
            zonedDateTimeBob,
            zonedDateTimeRequiredBob,
            localTimeBob,
            localTimeRequiredBob,
            durationBob,
            durationRequiredBob,
            booleanBob,
            booleanRequiredBob,
            enumBob,
            enumRequiredBob,
            uuidBob,
            uuidRequiredBob,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FieldTestServiceClassAndJpaFilteringEntityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStringBob().map(f -> "stringBob=" + f + ", ").orElse("") +
            optionalStringRequiredBob().map(f -> "stringRequiredBob=" + f + ", ").orElse("") +
            optionalStringMinlengthBob().map(f -> "stringMinlengthBob=" + f + ", ").orElse("") +
            optionalStringMaxlengthBob().map(f -> "stringMaxlengthBob=" + f + ", ").orElse("") +
            optionalStringPatternBob().map(f -> "stringPatternBob=" + f + ", ").orElse("") +
            optionalIntegerBob().map(f -> "integerBob=" + f + ", ").orElse("") +
            optionalIntegerRequiredBob().map(f -> "integerRequiredBob=" + f + ", ").orElse("") +
            optionalIntegerMinBob().map(f -> "integerMinBob=" + f + ", ").orElse("") +
            optionalIntegerMaxBob().map(f -> "integerMaxBob=" + f + ", ").orElse("") +
            optionalLongBob().map(f -> "longBob=" + f + ", ").orElse("") +
            optionalLongRequiredBob().map(f -> "longRequiredBob=" + f + ", ").orElse("") +
            optionalLongMinBob().map(f -> "longMinBob=" + f + ", ").orElse("") +
            optionalLongMaxBob().map(f -> "longMaxBob=" + f + ", ").orElse("") +
            optionalFloatBob().map(f -> "floatBob=" + f + ", ").orElse("") +
            optionalFloatRequiredBob().map(f -> "floatRequiredBob=" + f + ", ").orElse("") +
            optionalFloatMinBob().map(f -> "floatMinBob=" + f + ", ").orElse("") +
            optionalFloatMaxBob().map(f -> "floatMaxBob=" + f + ", ").orElse("") +
            optionalDoubleRequiredBob().map(f -> "doubleRequiredBob=" + f + ", ").orElse("") +
            optionalDoubleMinBob().map(f -> "doubleMinBob=" + f + ", ").orElse("") +
            optionalDoubleMaxBob().map(f -> "doubleMaxBob=" + f + ", ").orElse("") +
            optionalBigDecimalRequiredBob().map(f -> "bigDecimalRequiredBob=" + f + ", ").orElse("") +
            optionalBigDecimalMinBob().map(f -> "bigDecimalMinBob=" + f + ", ").orElse("") +
            optionalBigDecimalMaxBob().map(f -> "bigDecimalMaxBob=" + f + ", ").orElse("") +
            optionalLocalDateBob().map(f -> "localDateBob=" + f + ", ").orElse("") +
            optionalLocalDateRequiredBob().map(f -> "localDateRequiredBob=" + f + ", ").orElse("") +
            optionalInstantBob().map(f -> "instantBob=" + f + ", ").orElse("") +
            optionalInstanteRequiredBob().map(f -> "instanteRequiredBob=" + f + ", ").orElse("") +
            optionalZonedDateTimeBob().map(f -> "zonedDateTimeBob=" + f + ", ").orElse("") +
            optionalZonedDateTimeRequiredBob().map(f -> "zonedDateTimeRequiredBob=" + f + ", ").orElse("") +
            optionalLocalTimeBob().map(f -> "localTimeBob=" + f + ", ").orElse("") +
            optionalLocalTimeRequiredBob().map(f -> "localTimeRequiredBob=" + f + ", ").orElse("") +
            optionalDurationBob().map(f -> "durationBob=" + f + ", ").orElse("") +
            optionalDurationRequiredBob().map(f -> "durationRequiredBob=" + f + ", ").orElse("") +
            optionalBooleanBob().map(f -> "booleanBob=" + f + ", ").orElse("") +
            optionalBooleanRequiredBob().map(f -> "booleanRequiredBob=" + f + ", ").orElse("") +
            optionalEnumBob().map(f -> "enumBob=" + f + ", ").orElse("") +
            optionalEnumRequiredBob().map(f -> "enumRequiredBob=" + f + ", ").orElse("") +
            optionalUuidBob().map(f -> "uuidBob=" + f + ", ").orElse("") +
            optionalUuidRequiredBob().map(f -> "uuidRequiredBob=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
