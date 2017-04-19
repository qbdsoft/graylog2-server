package org.graylog2.lookup.adapters;

import com.google.auto.value.AutoValue;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.graylog.autovalue.WithBeanGetter;
import org.graylog2.plugin.lookup.LookupDataAdapter;
import org.graylog2.plugin.lookup.LookupDataAdapterConfiguration;

import java.security.SecureRandom;

public class RandomDataAdapter extends LookupDataAdapter {

    public static final String NAME = "random";
    private final SecureRandom secureRandom;
    private final Config config;

    @Inject
    public RandomDataAdapter(@Assisted LookupDataAdapterConfiguration config) {
        super(config);
        this.config = (Config) config;
        secureRandom = new SecureRandom();
    }

    @Override
    public Object get(Object key) {
        return secureRandom.ints(config.lowerBound(), config.upperBound()).findAny().getAsInt();
    }

    @Override
    public void set(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public interface Factory extends LookupDataAdapter.Factory<RandomDataAdapter> {
        @Override
        RandomDataAdapter create(@Assisted LookupDataAdapterConfiguration configuration);

        @Override
        Descriptor getDescriptor();
    }

    public static class Descriptor extends LookupDataAdapter.Descriptor<Config> {
        public Descriptor() {
            super(NAME, Config.class);
        }

        @Override
        public Config defaultConfiguration() {
            return Config.builder().type(NAME).lowerBound(0).upperBound(Integer.MAX_VALUE).build();
        }
    }

    @AutoValue
    @WithBeanGetter
    @JsonAutoDetect
    @JsonDeserialize(builder = AutoValue_RandomDataAdapter_Config.Builder.class)
    @JsonTypeName(NAME)
    public static abstract class Config implements LookupDataAdapterConfiguration {

        @Override
        @JsonProperty(TYPE_FIELD)
        public abstract String type();

        @JsonProperty("lower_bound")
        public abstract int lowerBound();

        @JsonProperty("upper_bound")
        public abstract int upperBound();

        public static Builder builder() {
            return new AutoValue_RandomDataAdapter_Config.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {
            @JsonProperty(TYPE_FIELD)
            public abstract Builder type(String type);

            @JsonProperty("lower_bound")
            public abstract Builder lowerBound(int lowerBound);

            @JsonProperty("upper_bound")
            public abstract Builder upperBound(int upperBound);

            public abstract Config build();
        }
    }
}
