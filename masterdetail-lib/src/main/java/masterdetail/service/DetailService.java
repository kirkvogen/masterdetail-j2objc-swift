/*
 * Copyright 2014-2015 Kirk C. Vogen
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
package masterdetail.service;

import java.util.List;

import masterdetail.model.DetailEntry;

/**
 * An interface for persisting items
 */
public interface DetailService {

    DetailEntry create();

    void delete (int id);

    void delete (DetailEntry detailEntry);

    void update (DetailEntry detailEntry);

    DetailEntry find(int id);

    Boolean exists(int id);

    List<DetailEntry> findAll();
}
