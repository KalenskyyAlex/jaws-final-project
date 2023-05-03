package com.bezkoder.spring.dynamodb.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.bezkoder.spring.dynamodb.model.TutorialDynamoDB;

@Repository
public class TutorialDynamoDBRepository {

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	private DynamoDB dynamoDB;

	public TutorialDynamoDBRepository(AmazonDynamoDB amazonDynamoDB) {
		dynamoDB = new DynamoDB(amazonDynamoDB);
	}

	public TutorialDynamoDB save(TutorialDynamoDB tutorial) {
		dynamoDBMapper.save(tutorial);
		return tutorial;

	}

	public TutorialDynamoDB getTutorialById(String tutorialId) {
		return dynamoDBMapper.load(TutorialDynamoDB.class, tutorialId);
	}

	public List<TutorialDynamoDB> getTutorialByTitel(String title) {

		PaginatedScanList<TutorialDynamoDB> tutorialList;
		if (title == null || title == "") {
			tutorialList = dynamoDBMapper.scan(TutorialDynamoDB.class, new DynamoDBScanExpression());
		} else {
			DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
			scanExpression.addFilterCondition("title",
					new Condition().withComparisonOperator(ComparisonOperator.CONTAINS)
							.withAttributeValueList(new AttributeValue().withS(title)));
			tutorialList = dynamoDBMapper.scan(TutorialDynamoDB.class, scanExpression);
		}

		tutorialList.loadAllResults();
		List<TutorialDynamoDB> list = new ArrayList<TutorialDynamoDB>(tutorialList.size());

		Iterator<TutorialDynamoDB> iterator = tutorialList.iterator();
		while (iterator.hasNext()) {
			TutorialDynamoDB element = iterator.next();
			list.add(element);
		}

		return list;

	}

	public String delete(String tutorialId) {
		TutorialDynamoDB tutorial = dynamoDBMapper.load(TutorialDynamoDB.class, tutorialId);
		dynamoDBMapper.delete(tutorial);

		return "tutorial Deleted!";
	}

	public TutorialDynamoDB update(String tutorialId, TutorialDynamoDB tutorial) {
		dynamoDBMapper.save(tutorial);
		return tutorial;
	}

	public List<TutorialDynamoDB> findByPublished(boolean b) {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		scanExpression.addFilterCondition("published", new Condition().withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(new AttributeValue().withBOOL(b)));
		PaginatedScanList<TutorialDynamoDB> tutorialList = dynamoDBMapper.scan(TutorialDynamoDB.class, scanExpression);

		tutorialList.loadAllResults();
		List<TutorialDynamoDB> list = new ArrayList<TutorialDynamoDB>(tutorialList.size());

		Iterator<TutorialDynamoDB> iterator = tutorialList.iterator();
		while (iterator.hasNext()) {
			TutorialDynamoDB element = iterator.next();
			list.add(element);
		}

		return list;
	}

}
