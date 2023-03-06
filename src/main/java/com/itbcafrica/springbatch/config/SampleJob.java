package com.itbcafrica.springbatch.config;

import com.itbcafrica.springbatch.listener.FirstJobListener;
import com.itbcafrica.springbatch.listener.FirstStepListener;
import com.itbcafrica.springbatch.model.*;
import com.itbcafrica.springbatch.processor.FirstItemProcessor;
import com.itbcafrica.springbatch.reader.FirstItemReader;
import com.itbcafrica.springbatch.service.SecondTasklet;
import com.itbcafrica.springbatch.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

@Configuration
public class SampleJob {
    @Autowired
    private FirstItemReader firstItemReader;

    /*   @Autowired
       private StudentService studentService;
   */
    @Autowired
    private DataSource dataSource;

    @Autowired
    private FirstItemProcessor firstItemProcessor;

    @Autowired
    private FirstItemWriter firstItemWriter;

    @Autowired
    private JobBuilderFactory jobBuilder;
    @Autowired
    private FirstJobListener firstJobListener;
    @Autowired
    private FirstStepListener firstStepListener;

    @Autowired
    private SecondTasklet secondTasklet;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job secondJob() {
        return jobBuilder.get ( "Second Job" )
                .incrementer ( new RunIdIncrementer () )
                .start ( firstChunkStep () )
                .build ();
    }

    @Bean
    public Job firstJob() {
        return jobBuilder.get ( "First Job" )
                .incrementer ( new RunIdIncrementer () )
                .start ( firstStep () )
                .next ( secondStep () )
                .listener ( firstJobListener )
                .build ();
    }

    private Step firstStep() {
        return stepBuilderFactory.get ( "First Step" )
                .tasklet ( firstTask () )
                .listener ( firstStepListener )
                .build ();
    }

    private Step secondStep() {
        return stepBuilderFactory.get ( "Second Step" )
                .tasklet ( secondTasklet ).build ();
    }

    private Tasklet firstTask() {
        return new Tasklet () {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println ( "this is first tasklet " );
                System.out.println ( chunkContext.getStepContext ().getJobExecutionContext () );
                return RepeatStatus.FINISHED;
            }
        };
    }

    private Tasklet secondTask() {
        return new Tasklet () {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println ( "this is second tasklet " );
                System.out.println ( chunkContext.getStepContext ().getJobExecutionContext () );
                return RepeatStatus.FINISHED;
            }
        };
    }

    private Step firstChunkStep() {
        return stepBuilderFactory.get ( "First Chunk  Step" )
                .<StudentCsv, StudentCsv>chunk ( 3 )
                .reader ( flatFileItemReader () )
                // .reader ( jdbcJdbcCursorItemReader () )
                //.processor ( firstItemProcessor )
                // .writer ( flatFileItemWriter () )
                // .writer ( jsonFileItemWriter () )
                //  .writer ( staxEventItemWriter () )
                // .writer ( jdbcBatchItemWriter () )
                .writer ( jdbcBatchItemWriter1 () )
                .build ();
    }


    public FlatFileItemReader<StudentCsv> flatFileItemReader() {
        FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<> ();

        flatFileItemReader.setResource ( new FileSystemResource (
                new File ( "C:\\Users\\bouendeu\\Desktop\\spring-batch\\InputFiles\\students.csv" )
        ) );

        DefaultLineMapper<StudentCsv> lineMapper = new DefaultLineMapper<> ();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer ();
        tokenizer.setNames ( "Id", "First Name", "Last Name", "Email" );
        lineMapper.setLineTokenizer ( tokenizer );
        BeanWrapperFieldSetMapper<StudentCsv> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<> ();
        beanWrapperFieldSetMapper.setTargetType ( StudentCsv.class );
        lineMapper.setFieldSetMapper ( beanWrapperFieldSetMapper );
        flatFileItemReader.setLineMapper ( lineMapper );
        flatFileItemReader.setLinesToSkip ( 1 );
        return flatFileItemReader;
    }

    public JsonItemReader<StudentJson> jsonJsonItemReader() {
        JsonItemReader jsonItemReader = new JsonItemReader ();
        jsonItemReader.setResource ( new FileSystemResource ( new File ( "C:\\Users\\bouendeu\\Desktop\\spring-batch\\InputFiles\\students.json" ) ) );
        jsonItemReader.setJsonObjectReader ( new JacksonJsonObjectReader<> ( StudentJson.class ) );

        return jsonItemReader;
    }


    public StaxEventItemReader<StudentXml> StaxEventItemReader() {
        StaxEventItemReader<StudentXml> itemReader = new StaxEventItemReader<> ();
        itemReader.setResource ( new FileSystemResource ( new File ( "C:\\Users\\bouendeu\\Desktop\\spring-batch\\InputFiles\\students.xml" ) ) );
        itemReader.setFragmentRootElementName ( "student" );
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller ();
        marshaller.setClassesToBeBound ( StudentXml.class );

        itemReader.setUnmarshaller ( marshaller );
        return itemReader;
    }

    public JdbcCursorItemReader<StudentJdbc> jdbcJdbcCursorItemReader() {
        JdbcCursorItemReader<StudentJdbc> itemReader = new JdbcCursorItemReader<> ();
        itemReader.setDataSource ( dataSource );
        itemReader.setSql ( "select id,first_name as firstName ,last_name as lastName,email from students" );
        BeanPropertyRowMapper<StudentJdbc> studentJdbcBeanPropertyRowMapper = new BeanPropertyRowMapper<> ();
        studentJdbcBeanPropertyRowMapper.setMappedClass ( StudentJdbc.class );
        itemReader.setRowMapper ( studentJdbcBeanPropertyRowMapper );
        return itemReader;
    }

    public ItemReaderAdapter<StudentResponse> itemReaderAdapter() {

        ItemReaderAdapter<StudentResponse> itemReaderAdapter = new ItemReaderAdapter<> ();
        // itemReaderAdapter.setTargetObject ( studentService );
        itemReaderAdapter.setTargetMethod ( "getStudent" );

        return itemReaderAdapter;
    }

    public FlatFileItemWriter<StudentJdbc> flatFileItemWriter() {

        FlatFileItemWriter<StudentJdbc> flatFileItemWriter = new FlatFileItemWriter<> ();
        flatFileItemWriter.setResource ( new FileSystemResource (
                new File ( "C:\\Users\\bouendeu\\Desktop\\spring-batch\\OutputFiles\\students.csv" )
        ) );

        flatFileItemWriter.setHeaderCallback ( new FlatFileHeaderCallback () {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write ( "Id,First Name,Last Name,Email" );
            }
        } );
        DelimitedLineAggregator<StudentJdbc> delimitedLineAggregator = new DelimitedLineAggregator<> ();
        BeanWrapperFieldExtractor<StudentJdbc> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<> ();
        beanWrapperFieldExtractor.setNames ( new String[]{"id", "firstName", "lastName", "email"} );
        delimitedLineAggregator.setFieldExtractor ( beanWrapperFieldExtractor );
        flatFileItemWriter.setLineAggregator ( delimitedLineAggregator );
        flatFileItemWriter.setFooterCallback ( new FlatFileFooterCallback () {
            @Override
            public void writeFooter(Writer writer) throws IOException {
                writer.write ( "Created @ " + new Date () );
            }
        } );
        return flatFileItemWriter;
    }

    public JsonFileItemWriter<StudentJson> jsonFileItemWriter() {

        FileSystemResource fileSystemResource = new FileSystemResource ( new File ( "C:\\Users\\bouendeu\\Desktop\\spring-batch\\OutputFiles\\students.json" ) );

        JsonFileItemWriter<StudentJson> jsonFileItemWriter = new JsonFileItemWriter<> ( fileSystemResource, new JacksonJsonObjectMarshaller<StudentJson> () );

        return jsonFileItemWriter;
    }

    public StaxEventItemWriter<StudentJdbc> staxEventItemWriter() {
        StaxEventItemWriter<StudentJdbc> staxEventItemWriter = new StaxEventItemWriter<> ();
        FileSystemResource fileSystemResource = new FileSystemResource ( new File ( "C:\\Users\\bouendeu\\Desktop\\spring-batch\\OutputFiles\\students.xml" ) );

        staxEventItemWriter.setResource ( fileSystemResource );
        staxEventItemWriter.setRootTagName ( "students" );
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller ();
        jaxb2Marshaller.setClassesToBeBound ( StudentJdbc.class );
        staxEventItemWriter.setMarshaller ( jaxb2Marshaller );

        return staxEventItemWriter;
    }

    @Bean
    public JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter() {

        JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter = new JdbcBatchItemWriter<> ();
        jdbcBatchItemWriter.setDataSource ( dataSource );
        jdbcBatchItemWriter.setSql (
                "insert into students (id,first_name,last_name,email)"
                        + "values(:id,:firstName,:lastName,:email)"
        );
        jdbcBatchItemWriter.setItemSqlParameterSourceProvider (
                new BeanPropertyItemSqlParameterSourceProvider<StudentCsv> () );
        return jdbcBatchItemWriter;
    }

    @Bean
    public JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter1() {

        JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter = new JdbcBatchItemWriter<> ();
        jdbcBatchItemWriter.setDataSource ( dataSource );
        jdbcBatchItemWriter.setSql (
                "insert into students (id,first_name,last_name,email)"
                        + "values(?,?,?,?)"
        );
        jdbcBatchItemWriter.setItemPreparedStatementSetter ( new ItemPreparedStatementSetter<StudentCsv> () {
            @Override
            public void setValues(StudentCsv studentCsv, PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setLong ( 1, studentCsv.getId () );
                preparedStatement.setString ( 2, studentCsv.getFirstName () );
                preparedStatement.setString ( 3, studentCsv.getLastName () );
                preparedStatement.setString ( 4, studentCsv.getEmail () );
            }
        } );
        return jdbcBatchItemWriter;
    }
}
