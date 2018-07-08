package jp.co.soramitsu.jackson;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import java.io.DataOutput;
import java.io.File;
import java.io.FileNotFoundException;
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
    throw new UnsupportedOperationException("unsupported createGenerator(OutputStream)");
  }

  @Override
  public OneCodeGenerator createGenerator(File f, JsonEncoding enc) throws FileNotFoundException {
    throw new UnsupportedOperationException("unsupported createGenerator(file)");
  }

  @Override
  public OneCodeGenerator createGenerator(OutputStream out, JsonEncoding enc) {
    Writer w = new OutputStreamWriter(out, DEFAULT_CHARSET);
    return new OneCodeGenerator(w, DEFAULT_CHARSET);
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
