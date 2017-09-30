
package edu.kit.jsoncore;

public class Selector {

	private String type;
	private String value;
	private RefinedBy_ refinedBy;
	private String conformsTo;
	private String exact;
	private String prefix;
	private String suffix;
	private Long start;
	private Long end;
	private StartSelector startSelector;
	private EndSelector endSelector;

	public StartSelector getStartSelector() {
		return startSelector;
	}

	public void setStartSelector(StartSelector startSelector) {
		this.startSelector = startSelector;
	}

	public EndSelector getEndSelector() {
		return endSelector;
	}

	public void setEndSelector(EndSelector endSelector) {
		this.endSelector = endSelector;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public String getExact() {
		return exact;
	}

	public void setExact(String exact) {
		this.exact = exact;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getConformsTo() {
		return conformsTo;
	}

	public void setConformsTo(String conformsTo) {
		this.conformsTo = conformsTo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public RefinedBy_ getRefinedBy() {
		return refinedBy;
	}

	public void setRefinedBy(RefinedBy_ refinedBy) {
		this.refinedBy = refinedBy;
	}

}
