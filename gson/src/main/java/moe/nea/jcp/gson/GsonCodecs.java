package moe.nea.jcp.gson;

import com.google.gson.JsonElement;
import moe.nea.pcj.json.BasicCodecs;

public class GsonCodecs extends BasicCodecs<JsonElement> {
	protected GsonCodecs() {}

	public static final GsonCodecs INSTANCE = new GsonCodecs();
}
