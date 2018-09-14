package jp.co.soramitsu.jackson;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * @deprecated because it can not writeJsonNode MAP objects properly. Fields will not be sorted even
 * if Jackon is told to do so.
 */
@Deprecated // since 2.0
public class OneCodeMapper extends ObjectMapper {

  public OneCodeMapper() {
    this(new OneCodeFactory());
  }

  protected OneCodeMapper(OneCodeFactory oneCodeFactory) {
    super(oneCodeFactory);

    registerModule(new JavaTimeModule());

    // all keys must be sorted alphabetically
    enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
    enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

    // time must be serialized as ISO8601 string with timezone
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    disable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);

    // nulls must not be included
    setSerializationInclusion(Include.NON_NULL);
  }
}
