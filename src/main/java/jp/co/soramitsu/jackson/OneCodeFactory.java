package jp.co.soramitsu.jackson;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

class OneCodeFactory extends JsonFactory {

  public static final Charset DEFAULT_CHARSET = UTF_8;

  public OneCodeFactory() {
    super(null);
  }

  @Override
  public OneCodeGenerator createGenerator(OutputStream out) {
    Writer w = new OutputStreamWriter(out, DEFAULT_CHARSET);
    return new OneCodeGenerator(w, DEFAULT_CHARSET);
  }

  @Override
  public OneCodeGenerator createGenerator(File f, JsonEncoding enc)
      throws IOException {
    FileWriter w = new FileWriter(f);
    return new OneCodeGenerator(w, DEFAULT_CHARSET);
  }

  @Override
  public OneCodeGenerator createGenerator(OutputStream out, JsonEncoding enc) {
    return createGenerator(out);
  }

  @Override
  public OneCodeGenerator createGenerator(Writer w) {
    return new OneCodeGenerator(w, DEFAULT_CHARSET);
  }

  @Override
  public OneCodeGenerator createGenerator(DataOutput out) {
    throw new UnsupportedOperationException("can not create generator from DataOutput");
  }
}
