package dev.decagon.godday.mvvm.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.decagon.godday.mvvm.model.Comment
import dev.decagon.godday.mvvm.model.Post
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MainDatabaseTest {

    private lateinit var commentdao: CommentDatabaseDao
    private lateinit var postdao: PostDatabaseDao
    private lateinit var db: MainDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, MainDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        commentdao = db.commentDatabaseDao
        postdao = db.postDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetComment() {
        val comment = Comment(1, null, "Godday", "g@g.com", "Hello")
        commentdao.insert(comment)
        val comment2 = commentdao.getLatestComment()
        assertEquals(comment2.name, "Godday")
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun insertAndGetPost() {
        val post = Post(1, null, "Hello", "This is testing")
        postdao.insert(post)
        val post2 = postdao.getLatestPost()
        assertEquals(post2.title, "Hello")
    }
}