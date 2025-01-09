package com.example.transaction.batch;

import com.example.transaction.dto.TransactionDTO;
import com.example.transaction.entity.Transaction;
import com.example.transaction.repository.TransactionRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    public Job importTransactionsJob() {
        return new JobBuilder("importTransactionsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importTransactionsStep())
                .build();
    }

    @Bean
    public Step importTransactionsStep() {
        return new StepBuilder("importTransactionsStep", jobRepository)
                .<TransactionDTO, Transaction>chunk(10, transactionManager)
                .reader(fileReader())
                .processor(transactionProcessor())
                .writer(transactionWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<TransactionDTO> fileReader() {
        FlatFileItemReader<TransactionDTO> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("dataSource.txt")); // Path to the file
        reader.setLineMapper(new DefaultLineMapper<TransactionDTO>() {{
            setLineTokenizer(lineTokenizer());
            setFieldSetMapper(fieldSetMapper());
        }});
        reader.setLinesToSkip(1);
        return reader;
    }

    @Bean
    public LineTokenizer lineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter("|");
        tokenizer.setNames("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId");
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<TransactionDTO> fieldSetMapper() {
        BeanWrapperFieldSetMapper<TransactionDTO> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(TransactionDTO.class);
        return mapper;
    }

    @Bean
    public ItemProcessor<TransactionDTO, Transaction> transactionProcessor() {
        return new ItemProcessor<TransactionDTO, Transaction>() {
            @Override
            public Transaction process(TransactionDTO record) throws Exception {
                Transaction transaction = new Transaction();
                transaction.setAccountNumber(record.getAccountNumber());
                transaction.setTrxAmount(record.getTrxAmount());
                transaction.setDescription(record.getDescription());
                // Convert trxDate from String to LocalDate
                if (record.getTrxDate() != null) {
                    transaction.setTrxDate(LocalDate.parse(record.getTrxDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }

                // Convert trxTime from String to LocalTime
                if (record.getTrxTime() != null) {
                    transaction.setTrxTime(LocalTime.parse(record.getTrxTime(), DateTimeFormatter.ofPattern("HH:mm:ss")));
                }
                transaction.setCustomerId(record.getCustomerId());
                return transaction;
            }
        };
    }

    @Bean
    public ItemWriter<Transaction> transactionWriter() {
        return new ListItemWriter<Transaction>() {

            @Autowired
            private TransactionRepository transactionRepository;

            @Override
            public void write(Chunk<? extends Transaction> items) {
                transactionRepository.saveAll(items);
            }
        };
    }
}

