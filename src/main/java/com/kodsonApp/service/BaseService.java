package com.kodsonApp.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Base service class with common CRUD operations and caching
 */
@Transactional(readOnly = true)
public abstract class BaseService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();
    protected abstract String getCacheName();
    protected abstract Class<T> getEntityClass();

    @Cacheable(value = "entities", key = "#root.target.cacheName + '_' + #id")
    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    @Cacheable(value = "entities", key = "#root.target.cacheName + '_all'")
    public List<T> findAll() {
        return getRepository().findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    @Transactional
    @CacheEvict(value = "entities", allEntries = true)
    public T save(T entity) {
        return getRepository().save(entity);
    }

    @Transactional
    @CacheEvict(value = "entities", allEntries = true)
    public List<T> saveAll(List<T> entities) {
        return getRepository().saveAll(entities);
    }

    @Transactional
    @CacheEvict(value = "entities", allEntries = true)
    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "entities", allEntries = true)
    public void delete(T entity) {
        getRepository().delete(entity);
    }

    public boolean existsById(ID id) {
        return getRepository().existsById(id);
    }

    public long count() {
        return getRepository().count();
    }
}
