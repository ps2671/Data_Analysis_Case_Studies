/*
 * Copyright (c) 2014, alex-ac
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package info.bem;

public abstract class TreeMatcher implements TreeProcessor {
  public static TreeMatcher Create(String arg) throws BEMException {
    if (arg.startsWith("[") && arg.endsWith("]")) {
      return MatcherForToken(arg.substring(1, arg.length() - 1));

    } else if (arg.startsWith(".")) {
      return MatcherForToken(arg.substring(1));
    }
    throw new BEMException("Wrong format of string: " + arg);
  }

  private static TreeMatcher MatcherForToken(String token) throws BEMException {
    if (token.matches("[0-9]+")) {
      return new ArrayTreeMatcher(Integer.parseUnsignedInt(token));
    } else if (token.matches("[a-zA-Z_][a-zA-Z_-]*")) {
      return new ObjectTreeMatcher(token);
    }
    throw new BEMException("Wrong format of string: " + token);
  }
}
