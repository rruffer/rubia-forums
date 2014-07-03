/*
 * Vige, Home of Professional Open Source
 * Copyright 2010, Vige, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vige.rubia.selenium.forum.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static org.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static org.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static org.vige.rubia.selenium.adminpanel.action.RemoveForum.removeForum;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_2_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static org.vige.rubia.selenium.forum.action.TopicType.ADVICE;
import static org.vige.rubia.selenium.forum.action.TopicType.IMPORTANT;
import static org.vige.rubia.selenium.forum.action.TopicType.NORMAL;
import static org.vige.rubia.selenium.forum.action.VerifyPost.getPostsOfTopics;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.vige.rubia.model.Post;
import org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest;

@RunWith(Arquillian.class)
public class OperationPostTest {

	@Drone
	private FirefoxDriver driver;

	@Before
	public void setUp() {
		driver.get("http://root:gtn@localhost:8080/rubia-forums/");
		String message = createCategory(driver, "First Test Category");
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = createCategory(driver, "Second Test Category");
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
		message = createForum(driver, "First Test Forum",
				"First Test Description", "First Test Category");
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		Map<File, String> files = new HashMap<File, String>();
		files.put(new File("/first"), "First Test File");
		files.put(new File("/second"), "Second Test File");
		files.put(new File("/third"), "Third Test File");
		message = createTopic(driver, "First Test Forum", "First Test Topic",
				"First Test Body", NORMAL, "First Test Question", new String[] {
						"First Test Answer", "Second Test Answer" }, 4, files);
		assertTrue(message.equals("First Test Topic"));
		message = createTopic(driver, "First Test Forum", "Second Test Topic",
				"Second Test Body", IMPORTANT, "Second Test Question",
				new String[] { "Third Test Answer", "Fourth Test Answer" }, 8,
				files);
		assertTrue(message.equals("Second Test Topic"));
		message = createForum(driver, "Second Test Forum",
				"Second Test Description", "First Test Category");
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(driver, "Second Test Forum", "Third Test Topic",
				"Third Test Body", ADVICE, "Third Test Question", new String[] {
						"Fifth Test Answer", "Sixth Test Answer" }, 9, files);
		assertTrue(message.equals("Third Test Topic"));
		files.clear();
		files.put(new File("/fourth"), "Fourth Test File");
		files.put(new File("/fifth"), "Fifth Test File");
		files.put(new File("/sixth"), "Sixth Test File");
		message = createTopic(driver, "Second Test Forum", "Fourth Test Topic",
				"Fourth Test Body", IMPORTANT, "Fourth Test Question",
				new String[] { "Seventh Test Answer", "Eight Test Answer" }, 0,
				files);
		assertTrue(message.equals("Fourth Test Topic"));
	}

	@Test
	public void verifyCreatedPosts() {
		List<Post> posts = getPostsOfTopics(driver, "First Test Forum",
				"Second Test Forum");
		assertEquals(posts.size(), 4);
		Date today = new Date();

		Post secondTestPost = posts.get(0);
		assertTrue(secondTestPost.getCreateDate().after(today));
		assertEquals(secondTestPost.getEditCount(), 0);
		assertEquals(secondTestPost.getEditDate(), 0);
		assertEquals(secondTestPost.getMessage(), 0);
		assertEquals(secondTestPost.getPoster(), 0);
		assertEquals(secondTestPost.getTopic(), 0);
		assertEquals(secondTestPost.getUser(), 0);

		Post firstTestPost = posts.get(1);
		assertTrue(firstTestPost.getCreateDate().after(today));
		assertEquals(firstTestPost.getEditCount(), 0);
		assertEquals(firstTestPost.getEditDate(), 0);
		assertEquals(firstTestPost.getMessage(), 0);
		assertEquals(firstTestPost.getPoster(), 0);
		assertEquals(firstTestPost.getTopic(), 0);
		assertEquals(firstTestPost.getUser(), 0);

		Post thirdTestPost = posts.get(2);
		assertTrue(thirdTestPost.getCreateDate().after(today));
		assertEquals(thirdTestPost.getEditCount(), 0);
		assertEquals(thirdTestPost.getEditDate(), 0);
		assertEquals(thirdTestPost.getMessage(), 0);
		assertEquals(thirdTestPost.getPoster(), 0);
		assertEquals(thirdTestPost.getTopic(), 0);
		assertEquals(thirdTestPost.getUser(), 0);

		Post fourthTestPost = posts.get(3);
		assertTrue(fourthTestPost.getCreateDate().after(today));
		assertEquals(fourthTestPost.getEditCount(), 0);
		assertEquals(fourthTestPost.getEditDate(), 0);
		assertEquals(fourthTestPost.getMessage(), 0);
		assertEquals(fourthTestPost.getPoster(), 0);
		assertEquals(fourthTestPost.getTopic(), 0);
		assertEquals(fourthTestPost.getUser(), 0);
	}

	@After
	public void stop() {
		String message = removeTopic(driver, "First Test Forum",
				"First Test Topic", "First Test Body");
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, "First Test Forum", "Second Test Topic",
				"Second Test Body");
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, "Second Test Forum", "Third Test Topic",
				"Third Test Body");
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, "Second Test Forum", "Fourth Test Topic",
				"Fourth Test Body");
		assertTrue(message.equals("OK"));
		message = removeForum(driver, "First Test Forum", "Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, "Second Test Forum", SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, "First Test Category",
				AdminPanelCategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, "Second Test Category",
				AdminPanelCategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
