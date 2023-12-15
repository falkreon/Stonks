package blue.endless.stonks.model;

import java.util.Locale;

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.annotation.Deserializer;
import blue.endless.jankson.annotation.Serializer;

/**
 * Represents a kind of loyalty reward paid to shareholders of some investment asset by the issuer or fund manager. This
 * reward can be paid in cash or stocks, and for the purposes of this assignment is awarded once each quarter as a
 * percentage of the shares held.
 */
public record Dividend(double percentage, DividendType type) {
	
	@Serializer
	public JsonObject toJson() {
		JsonObject result = new JsonObject();
		
		result.put("percentage", JsonPrimitive.of(percentage));
		result.put("type", JsonPrimitive.of(type.name().toLowerCase(Locale.ROOT)));
		
		return result;
	}
	
	@Deserializer
	public static Dividend fromJson(JsonObject obj) {
		double percent = obj.getDouble("percentage", 0d);
		DividendType type = DividendType.valueOf(obj.get(String.class, "type").toUpperCase(Locale.ROOT));
		return new Dividend(percent, type);
	}
}
