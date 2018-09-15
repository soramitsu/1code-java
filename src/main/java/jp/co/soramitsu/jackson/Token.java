package jp.co.soramitsu.jackson;

import lombok.Getter;

@Getter
public enum Token {

  OBJECT_START('d'),
  OBJECT_END('e'),
  ARRAY_START('l'),
  ARRAY_END('e'),
  INTEGER_START('i'),
  INTEGER_END('e'),
  TRUE('T'),
  FALSE('F'),
  COLON(':'),
  NULL('N');

  private char symbol;

  Token(char f) {
    this.symbol = f;
  }
}
