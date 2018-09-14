package jp.co.soramitsu.jackson;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jp.co.soramitsu.jackson.Token.FALSE;
import static jp.co.soramitsu.jackson.Token.NULL;
import static jp.co.soramitsu.jackson.Token.TRUE;

import com.fasterxml.jackson.databind.JsonNode;
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
        writeJsonObject(node);
        break;
      case ARRAY:
        writeJsonArray(node);
        break;
      case BINARY:
        write(node.binaryValue());
        break;
      case BOOLEAN:
        writeBoolean(node.asBoolean());
        break;
      case STRING:
        write(node.asText());
        break;
      case NUMBER:
        writeJsonNumber(node);
        break;
      case NULL:
        write(NULL);
        break;

      default:
        /*
          Jackson-specific types are NULL, POJO, MISSING.
          Here we just ignore them.
         */
        break;
    }
  }


  public void writeJsonObject(JsonNode obj) throws IOException {
    assert obj.isObject();

    write(Token.OBJECT_START);

    Map<String, JsonNode> sorted = new TreeMap<>();
    obj.fields().forEachRemaining(f -> {
      sorted.put(f.getKey(), f.getValue());
    });

    for (Entry<String, JsonNode> field : sorted.entrySet()) {
      // stream can be used, but following functions throw checked exception
      write(field.getKey()); // throws
      writeJsonNode(field.getValue()); // throws
    }

    write(Token.OBJECT_END);
  }

  public void writeJsonArray(JsonNode arr) throws IOException {
    assert arr.isArray();

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
        write(node.intValue());
        break;
      case LONG:
        write(node.longValue());
        break;
      case BIG_INTEGER:
        write(node.bigIntegerValue());
        break;
      case FLOAT:
        write(node.floatValue());
        break;
      case DOUBLE:
        write(node.doubleValue());
        break;
      case BIG_DECIMAL:
        write(node.decimalValue());
        break;
    }

  }

  public void write(final Number number) throws IOException {
    write(Token.INTEGER_START);
    writer.write(String.valueOf(number));
    write(Token.INTEGER_END);
  }

  public void write(Token token) throws IOException {
    writer.write(token.getSymbol());
  }

  public void write(final String s) throws IOException {
    write(s.getBytes(charset));
  }

  // same as string
  public void write(final char[] c) throws IOException {
    byte[] bytes = charset.encode(CharBuffer.wrap(c)).array();
    write(bytes);
  }

  // same as string
  public void write(final byte[] b) throws IOException {
    writer.write(String.valueOf(b.length));
    write(Token.COLON);
    writer.write(new String(b, charset));
  }

  public void writeBoolean(boolean b) throws IOException {
    write(b ? TRUE : FALSE);
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
