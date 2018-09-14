package jp.co.soramitsu.jackson;

import static jp.co.soramitsu.jackson.Token.ARRAY_END;
import static jp.co.soramitsu.jackson.Token.ARRAY_START;
import static jp.co.soramitsu.jackson.Token.OBJECT_END;
import static jp.co.soramitsu.jackson.Token.OBJECT_START;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@Deprecated // since 2.0
@RequiredArgsConstructor
public class OneCodeGenerator extends JsonGenerator {

  private final OneCoder cdr;
  private ObjectCodec codec;

  @Override
  public JsonGenerator setCodec(ObjectCodec oc) {
    codec = oc;
    return this;
  }

  @Override
  public ObjectCodec getCodec() {
    return codec;
  }

  @Override
  public Version version() {
    throw new UnsupportedOperationException("can not call version()");
  }

  @Override
  public JsonGenerator enable(Feature f) {
    /* just ignore features */
    return this;
  }

  @Override
  public JsonGenerator disable(Feature f) {
    /* just ignore features */
    return this;
  }

  @Override
  public boolean isEnabled(Feature f) {
    throw new UnsupportedOperationException("can not execute isEnabled()");

  }

  @Override
  public int getFeatureMask() {
    throw new UnsupportedOperationException("can not execute getFeatureMask()");
  }

  @Override
  public JsonGenerator setFeatureMask(int values) {
    throw new UnsupportedOperationException(
        "can not execute setFeatureMask(" + values + ")");
  }

  @Override
  public JsonGenerator useDefaultPrettyPrinter() {
    throw new UnsupportedOperationException("can not execute useDefaultPrettyPrinter()");
  }

  @Override
  public void writeStartArray() throws IOException {
    cdr.write(ARRAY_START);
  }

  @Override
  public void writeEndArray() throws IOException {
    cdr.write(ARRAY_END);
  }

  @Override
  public void writeStartObject() throws IOException {
    cdr.write(OBJECT_START);
  }

  @Override
  public void writeEndObject() throws IOException {
    cdr.write(OBJECT_END);
  }

  @Override
  public void writeFieldName(String name) throws IOException {
    writeString(name);
  }

  @Override
  public void writeFieldName(SerializableString name) throws IOException {
    cdr.write(name.getValue());
  }

  @Override
  public void writeString(String text) throws IOException {
    cdr.write(text);
  }

  @Override
  public void writeString(char[] text, int offset, int len) throws IOException {
    writeRawValue(text, offset, len);
  }

  @Override
  public void writeString(SerializableString text) throws IOException {
    cdr.write(text.getValue());
  }

  @Override
  public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
    byte[] substring = Arrays.copyOfRange(text, offset, length);
    cdr.write(substring);
  }

  @Override
  public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
    byte[] substring = Arrays.copyOfRange(text, offset, length);
    cdr.write(substring);
  }

  @Override
  public void writeRaw(String text) throws IOException {
    cdr.write(text);
  }

  @Override
  public void writeRaw(String text, int offset, int len) throws IOException {
    writeRawValue(text, offset, len);
  }

  @Override
  public void writeRaw(char[] text, int offset, int len) throws IOException {
    writeRawValue(text, offset, len);
  }

  @Override
  public void writeRaw(char c) throws IOException {
    // write single char as string
    cdr.write("" + c);
  }

  @Override
  public void writeRawValue(String text) throws IOException {
    cdr.write(text);
  }

  @Override
  public void writeRawValue(String text, int offset, int len) throws IOException {
    String substring = text.substring(offset, len);
    cdr.write(substring);
  }

  @Override
  public void writeRawValue(char[] text, int offset, int len) throws IOException {
    char[] substring = Arrays.copyOfRange(text, offset, len);
    cdr.write(substring);
  }

  @Override
  public void writeBinary(Base64Variant bv, byte[] data, int offset, int len) throws IOException {
    writeRaw(bv.encode(data));
  }

  @Override
  public int writeBinary(Base64Variant bv, InputStream data, int dataLength) throws IOException {
    throw new UnsupportedOperationException("can not execute writeBinary()");
  }

  @Override
  public void writeNumber(int v) throws IOException {
    cdr.write(v);
  }

  @Override
  public void writeNumber(long v) throws IOException {
    cdr.write(v);
  }

  @Override
  public void writeNumber(BigInteger v) throws IOException {
    cdr.write(v);
  }

  @Override
  public void writeNumber(double v) throws IOException {
    cdr.write(v);
  }

  @Override
  public void writeNumber(float v) throws IOException {
    cdr.write(v);
  }

  @Override
  public void writeNumber(BigDecimal v) throws IOException {
    cdr.write(v);
  }

  @Override
  public void writeNumber(String encodedValue) throws IOException {
    cdr.write(encodedValue);
  }

  @Override
  public void writeBoolean(boolean state) throws IOException {
    cdr.writeBoolean(state);
  }

  @Override
  public void writeNull() {
    /* ignore null */
  }

  @Override
  public void writeObject(Object pojo) throws IOException {
    throw new UnsupportedOperationException("can not write Object");
  }

  @Override
  public void writeTree(TreeNode rootNode) throws IOException {
    throw new UnsupportedOperationException("can not write TreeNode");
  }

  @Override
  public JsonStreamContext getOutputContext() {
    // should be null, otherwise json processor tries to disable some feature and throws exception
    return null;
  }

  @Override
  public void flush() throws IOException {
    cdr.flush();
  }

  @Override
  public boolean isClosed() {
    return cdr.isClosed();
  }

  @Override
  public void close() throws IOException {
    cdr.close();
  }
}
