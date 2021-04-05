package com.springbatch.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.springbatch.model.Employee;
import com.springbatch.model.Profile;
import com.springbatch.processor.EmployeeItemProcessor;

@Configuration
@EnableBatchProcessing
@ComponentScan("com.springbatch.*")
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	
	
	@Bean
    public Job readCSVFileJob(Step step1) {
        return jobBuilderFactory
                .get("readCSVFileJob")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }
	
	@Bean
    public Step step(ItemWriter<Profile> writer) {
        return stepBuilderFactory
                .get("step")
                .<Employee, Profile>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
	
	 @Bean
	    public ItemProcessor<Employee, Profile> processor() {
	        return new EmployeeItemProcessor();
	    }
	 
	 @Bean
	    public FlatFileItemReader<Employee> reader() {
	        FlatFileItemReader<Employee> itemReader = new FlatFileItemReader<Employee>();
	        itemReader.setLineMapper(lineMapper());
	        itemReader.setLinesToSkip(1);
	        itemReader.setResource(new ClassPathResource("employees.csv"));
	        return itemReader;
	    }
	 
	 @Bean
	    public LineMapper<Employee> lineMapper() {
	        DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<Employee>();
	        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
	        lineTokenizer.setNames(new String[] { "empCode", "empName", "expInYears" });
	        lineTokenizer.setIncludedFields(new int[] { 0, 1, 2 });
	        BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<Employee>();
	        fieldSetMapper.setTargetType(Employee.class);
	        lineMapper.setLineTokenizer(lineTokenizer);
	        lineMapper.setFieldSetMapper(fieldSetMapper);
	        return lineMapper;
	    }
	 
	 @Bean
		public JdbcBatchItemWriter<Profile> writer1(DataSource dataSource) {
			return new JdbcBatchItemWriterBuilder<Profile>()
			   .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Profile>())
			   .sql("INSERT INTO profile (empCode, empName, profileName) VALUES (:empCode, :empName, :profileName)")
			   .dataSource(dataSource)
			   .build();
		}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	 
	



}