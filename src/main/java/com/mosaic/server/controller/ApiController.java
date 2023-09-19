////////////////////////////////////////////////////////////////////////////////
// mosaic - a tile map server                                                  /
// Copyright (C) 2023 Evan Jaramillo                                           /
//                                                                             /
// This program is free software: you can redistribute it and/or modify        /
// it under the terms of the GNU General Public License as published by        /
// the Free Software Foundation, either version 3 of the License, or           /
// (at your option) any later version.                                         /
//                                                                             /
// This program is distributed in the hope that it will be useful,             /
// but WITHOUT ANY WARRANTY; without even the implied warranty of              /
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the               /
// GNU General Public License for more details.                                /
//                                                                             /
// You should have received a copy of the GNU General Public License           /
// along with this program.  If not, see <https://www.gnu.org/licenses/>.      /
////////////////////////////////////////////////////////////////////////////////

package com.mosaic.server.controller;

import com.mosaic.server.service.MosaicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/api")
public class ApiController {

    private final MosaicService service;
    @Autowired
    public ApiController(MosaicService service) {
        this.service = service;
    }
    @RequestMapping(path = "/tiles/{name}/{z}/{x}/{y}/data")
    @CrossOrigin(origins = "*")
    public ResponseEntity<byte[]> getTileData(@PathVariable("name") String name,
                                              @PathVariable("z") int z,
                                              @PathVariable("x") int x,
                                              @PathVariable("y") int y) {


        return new ResponseEntity<>(service.getTileData(name, z, x, y), HttpStatus.OK);

    }

}
