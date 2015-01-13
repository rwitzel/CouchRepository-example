package org.bitbucket.rwitzel.couchrepositoryexample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.bitbucket.rwitzel.couchrepositoryexample.BlogPost;
import org.bitbucket.rwitzel.couchrepositoryexample.SpringConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the repository for {@link BlogPost blog posts}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfig.class })
public class BlogPostTest {

    @Autowired
    private CrudRepository<BlogPost, String> blogPostRepo;

    @Test
    public void testSaveFindDeleteBlogPost() throws Exception {

        // given
        BlogPost newBlogPost = new BlogPost();
        newBlogPost.setId("my first blog post");

        // when
        BlogPost existingBlogPost = blogPostRepo.findOne(newBlogPost.getId());
        if (existingBlogPost != null) {
            blogPostRepo.delete(existingBlogPost);
        }

        // then
        assertNull(blogPostRepo.findOne(newBlogPost.getId()));

        // when
        blogPostRepo.save(newBlogPost);

        // then
        assertTrue(blogPostRepo.exists(newBlogPost.getId()));
        BlogPost foundBlogPost = blogPostRepo.findOne(newBlogPost.getId());
        assertNotNull(foundBlogPost);

        assertEquals(newBlogPost.getId(), foundBlogPost.getId());
        assertEquals(newBlogPost.getTags(), foundBlogPost.getTags());
        assertEquals(newBlogPost.getType(), foundBlogPost.getType());

        // when
        blogPostRepo.delete(foundBlogPost);

        // then
        assertFalse(blogPostRepo.exists(newBlogPost.getId()));
        assertNull(blogPostRepo.findOne(foundBlogPost.getId()));
    }
}
