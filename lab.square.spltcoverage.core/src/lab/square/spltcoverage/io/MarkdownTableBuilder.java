package lab.square.spltcoverage.io;

public class MarkdownTableBuilder {
	
	private static final String SEPARATOR = "|";
	private static final String TABLE_DIIVDER = "---";
	
	private StringBuilder content;
	private int column;
	
	public MarkdownTableBuilder(int column) {
		content = new StringBuilder();
		this.column = column;
	}
	
	public MarkdownTableBuilder addHeader(String... headers) {
		if(headers.length != column) {
			// TODO: throws exception;
			;
		}
		
		content.append(SEPARATOR);
		for(String header : headers) {
			content.append(header);
			content.append(SEPARATOR);
		}
		content.append(System.lineSeparator());
		
		content.append(SEPARATOR);
		for(int i = 0; i < headers.length; i++) {
			content.append(TABLE_DIIVDER);
			content.append(SEPARATOR);
		}
		content.append(System.lineSeparator());
		
		return this;
	}
	
	public MarkdownTableBuilder addRow(String... columns) {
		if(columns.length != column) {
			// TODO: throws exception;
			;
		}
		
		content.append(SEPARATOR);
		for(String column : columns) {
			content.append(column);
			content.append(SEPARATOR);
		}
		content.append(System.lineSeparator());
		
		return this;
	}
	
	public String build() {
		return content.toString();
	}
	
	
}
