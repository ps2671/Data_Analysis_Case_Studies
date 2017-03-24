/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.bem;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alex
 */
public interface TreeProcessor {

  public Object process(JSONObject dictionary) throws BEMException;

  public Object process(JSONArray array) throws BEMException;

  public Object process(String string) throws BEMException;

  public Object process(Number number) throws BEMException;

  public Object process(Boolean bool) throws BEMException;

  public Object process(Object object) throws BEMException;
}
