package org.laeq.db

import org.laeq.model.Category
import org.laeq.model.CategoryCollection

class CategoryCollectionDAOTest extends AbstractDAOTest {
    CategoryCollectionDAO repository;

    def setup(){
        repository = new CategoryCollectionDAO(manager, CategoryCollection.sequence_name)
    }

    def "test get next id"() {
        when:
        repository.getNextValue()
        repository.getNextValue()
        int result = repository.getNextValue()

        then:
        result == 3
    }

    def "test init"(){
        when:
        repository.init();

        then:
        notThrown Exception
    }

    def "test insertion"() {
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        def entity = new CategoryCollection()
        entity.setName("Mock NAME")
        entity.setIsDefault(false)
        entity.addCategory(new Category(1, "Moving truck", "icon/icon1.png", "#FFFFFF","A"))
        entity.addCategory(new Category(2, "Moving car", "icon/icon2.png", "#FFFFFF","B"))
        entity.addCategory(new Category(3, "Moving bike", "icon/icon3.png", "#FFFFFF","C"))
        entity.addCategory(new Category(4, "Moving bus", "icon/icon4.png",  "#FFFFFF","D"))


        when:
        repository.insert(entity)

        then:
        entity.id == 4
    }

    def "test update: modify name"() {
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        def entity = repository.findByID(1);
        entity.setName("Mock name")

        when:
        repository.update(entity)

        then:
        notThrown DAOException
    }

    def "test update: delete 2 categories"() {
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        def entity = repository.findByID(1);

        entity.removeCategory(1)
        entity.removeCategory(3)

        println entity.categorySet.size()


        when:
        repository.update(entity)
        repository.findCollectionIdsById(1) == [2,4]

        then:
        notThrown DAOException
    }

    def "test update: delete 2 categories add 2 new ones"() {
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        def entity = repository.findByID(2);

        entity.removeCategory(2)
        entity.removeCategory(3)

        println entity.categorySet.size()

        entity.addCategory(new Category(1, "Moving", "Moving","F000000" , "A"))
        entity.addCategory(new Category(4, "Moving", "Moving", "F000000","A"))


        when:
        repository.update(entity)
        repository.findCollectionIdsById(2) == [1, 4]

        then:
        notThrown DAOException
    }

    def "test: get list of ids of category for a specific collection."(){
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        when:
        def result = repository.findCollectionIdsById(1)

        then:
        result.collect{it} == [1,2,3,4]
    }

    def "test insertion with an invalid category"(){
        setup:
        CategoryCollection entity = new CategoryCollection()
        entity.setName("mock")
        entity.addCategory(new Category(-1, "test", "test", "", "A"))

        when:
        repository.insert(entity)

        then:
        thrown DAOException
    }

    def "test findById"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        when:
        CategoryCollection result = repository.findByID(1)

        def expected = new CategoryCollection(1, "Default", false)
        expected.addCategory(new Category(1, "Moving truck", "icon/icon1.png",  "#FFFFFF","A"))
        expected.addCategory(new Category(2, "Moving car", "icon/icon2.png", "#FFFFFF","B"))
        expected.addCategory(new Category(3, "Moving bike", "icon/icon3.png",  "#FFFFFF","C"))
        expected.addCategory(new Category(4, "Moving bus", "icon/icon4.png",  "#FFFFFF","D"))

        then:
        result == expected

    }

    def "test findDefault"(){
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        when:
        CategoryCollection result = repository.findDefault()

        then:
        result == new CategoryCollection(1, "Default", true)
    }

    def "test findAll but empty"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        when:
        def result = repository.findAll()

        then:
        result.size() == 3

    }

    def "test delete an existing category collection"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        CategoryCollection categoryCollection = new CategoryCollection(1, "mock", false)

        when:
        repository.delete(categoryCollection)

        then:
        thrown DAOException
    }

    def "test delete default category collection"() {
        when:
        repository.delete(new CategoryCollection(1, "mock", false))

        then:
        thrown DAOException
    }

    def "test delete an unknown category" (){
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        CategoryCollection categoryCollection = new CategoryCollection(100, "mock", false)

        when:
        repository.delete(categoryCollection)

        then:
        thrown DAOException
    }
}