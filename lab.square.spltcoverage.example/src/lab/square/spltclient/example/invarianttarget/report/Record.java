package lab.square.spltclient.example.invarianttarget.report;

public class Record {

//	public List <Report> report= new ArrayList<>(); 
//	
//	
//	public void record() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
//		Writer writer = Files.newBufferedWriter(Paths.get("date.csv"));
//        StatefulBeanToCsv<Report> beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
//
//        beanToCsv.write(report);
//
//        writer.flush();
//        writer.close();
//	}
//	public void record2() throws IOException {
//		String[] cabecalho = {"configuration", "trace", "description"};
//
//        List<String[]> linhas = new ArrayList<>();
//        for (Report r : report) {
//        linhas.add(new String[]{r.configuration,r.trace, r.description});
//        }
//        Writer writer = Files.newBufferedWriter(Paths.get("date.csv"));
//        CSVWriter csvWriter = new CSVWriter(writer);
//
//        csvWriter.writeNext(cabecalho);
//        csvWriter.writeAll(linhas);
//
//        csvWriter.flush();
//        writer.close();
//	}
}
