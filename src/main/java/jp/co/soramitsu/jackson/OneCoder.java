package jp.co.soramitsu.jackson;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jp.co.soramitsu.jackson.Token.COLON;
import static jp.co.soramitsu.jackson.Token.FALSE;
import static jp.co.soramitsu.jackson.Token.INTEGER_END;
import static jp.co.soramitsu.jackson.Token.INTEGER_START;
import static jp.co.soramitsu.jackson.Token.NULL;
import static jp.co.soramitsu.jackson.Token.TRUE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OneCoder {

  private Writer writer = new StringWriter();
  private Charset charset = UTF_8;

  @Override
  public String toString() {
    return writer.toString();
  }

  public void writeJsonNode(JsonNode node) throws IOException {
    switch (node.getNodeType()) {
      case OBJECT:
        writeJsonObject((ObjectNode) node);
        break;
      case ARRAY:
        writeJsonArray((ArrayNode) node);
        break;
      case BINARY:
        writeString(node.binaryValue());
        break;
      case BOOLEAN:
        writeBoolean(node.asBoolean());
        break;
      case STRING:
        writeString(node.asText());
        break;
      case NUMBER:
        writeJsonNumber(node);
        break;
      case NULL:
        writeNull();
        break;

      default:
        /*
          Jackson-specific types are NULL, POJO, MISSING.
          Here we just ignore them.
         */
        break;
    }
  }


  public void writeJsonObject(ObjectNode obj) throws IOException {
    write(Token.OBJECT_START);

    Map<String, JsonNode> sorted = new TreeMap<>();
    obj.fields().forEachRemaining(f -> sorted.put(f.getKey(), f.getValue()));

    for (Entry<String, JsonNode> field : sorted.entrySet()) {
      // stream can be used, but following functions throw checked exception
      writeString(field.getKey()); // throws
      writeJsonNode(field.getValue()); // throws
    }

    write(Token.OBJECT_END);
  }

  public void writeJsonArray(ArrayNode arr) throws IOException {
    write(Token.ARRAY_START);

    for (int i = 0, size = arr.size(); i < size; i++) {
      writeJsonNode(arr.get(i));
    }

    write(Token.ARRAY_END);
  }

  public void writeJsonNumber(JsonNode node) throws IOException {
    assert node.isNumber();

    switch (node.numberType()) {
      case INT:
        writeNumber(node.intValue());
        break;
      case LONG:
        writeNumber(node.longValue());
        break;
      case BIG_INTEGER:
        writeNumber(node.bigIntegerValue());
        break;
      case FLOAT:
        writeNumber(node.floatValue());
        break;
      case DOUBLE:
        writeNumber(node.doubleValue());
        break;
      case BIG_DECIMAL:
        writeNumber(node.decimalValue());
        break;
      default:
        /*
         no other cases, but default is required by codestyle
         */
        break;
    }

  }

  public void writeNumber(final Number number) throws IOException {
    write(INTEGER_START);
    writer.write(String.valueOf(number));
    write(INTEGER_END);
  }

  /* default */ void write(Token token) throws IOException {
    writer.write(token.getSymbol());
  }

  public void writeString(final String s) throws IOException {
    writeString(s.getBytes(charset));
  }

  // same as string
  public void writeString(final char[] c) throws IOException {
    byte[] bytes = charset.encode(CharBuffer.wrap(c)).array();
    writeString(bytes);
  }

  // same as string
  public void writeString(final byte[] b) throws IOException {
    writer.write(String.valueOf(b.length));
    write(COLON);
    writer.write(new String(b, charset));
  }

  public void writeBoolean(boolean b) throws IOException {
    write(b ? TRUE : FALSE);
  }

  public void writeNull() throws IOException {
    write(NULL);
  }

  public void flush() throws IOException {
    this.writer.flush();
  }

  public void close() throws IOException {
    this.writer.close();
  }

  public boolean isClosed() {
    try {
      this.flush();
      return false;
    } catch (IOException e) {
      return true;
    }
  }

}
