/*
 * Copyright (C) 2011 Benoit GUEROUT <bguerout at gmail dot com> and Yves AMSELLEM <amsellem dot yves at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jongo;

import com.mongodb.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;

public class MongoCollection {

    public static final String MONGO_ID = "_id";
    private final DBCollection collection;
    private final DBObjectConvertor convertor;

    public MongoCollection(String database, String collection) throws UnknownHostException, MongoException {
        this.collection = new Mongo().getDB(database).getCollection(collection);
        this.convertor = new DBObjectConvertor();
    }

    public <T> Iterator<T> find(String query, Class<T> clazz) {
        DBCursor cursor = collection.find(convertor.convert(query));
        return new MongoIterator<T>(cursor, clazz);
    }

    public <D> void save(D document) throws IOException {
        collection.save(convertor.convert(document));
    }

    public void drop() {
        collection.drop();
    }

    public <T> T findOne(String query, ResultMapper<T> resultMapper) {
        DBObject result = collection.findOne(convertor.convert(query));
        return resultMapper.map(result);
    }
}