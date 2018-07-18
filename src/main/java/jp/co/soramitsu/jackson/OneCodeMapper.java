package jp.co.soramitsu.jackson;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class OneCodeMapper extends ObjectMapper {

  public OneCodeMapper() {
    this(new OneCodeFactory());
  }

  public OneCodeMapper(OneCodeFactory oneCodeFactory) {
    super(oneCodeFactory);

    // all keys must be sorted alphabetically
    enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
    enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

    // nulls must not be included
    setSerializationInclusion(Include.NON_NULL);
  }
}
