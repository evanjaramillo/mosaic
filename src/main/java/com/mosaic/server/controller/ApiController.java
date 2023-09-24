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

import com.mosaic.server.interfaces.ILayer;
import com.mosaic.server.interfaces.IMbTilesMetadata;
import com.mosaic.server.mbtiles.MbTilesMetadata;
import com.mosaic.server.service.MosaicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/mbtiles")
public class ApiController {

    private final MosaicService service;
    private final HttpHeaders terrainHttpHeaders;

    @Autowired
    public ApiController(MosaicService service) {
        this.service = service;

        terrainHttpHeaders = new HttpHeaders();
        terrainHttpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        terrainHttpHeaders.add(HttpHeaders.CONTENT_ENCODING, "gzip");
    }


    @RequestMapping("/{name}/metadata")
    public ResponseEntity<IMbTilesMetadata> getMetadata(@PathVariable("name") String name) {
        return new ResponseEntity<>(service.getMetadata(name), HttpStatus.OK);
    }

    @RequestMapping(value = "/{name}/layer.json")
    public ResponseEntity<ILayer> getTerrainLayerSpec(@PathVariable("name") String name) {
        return new ResponseEntity<>(service.getTerrainLayerSpec(name), HttpStatus.OK);
    }

    @RequestMapping(path = "/{name}/{z}/{x}/{y}/data")
    public ResponseEntity<byte[]> getTileData(@PathVariable("name") String name,
                                              @PathVariable("z") int z,
                                              @PathVariable("x") int x,
                                              @PathVariable("y") int y) {


        return new ResponseEntity<>(service.getTileData(name, z, x, y), HttpStatus.OK);

    }

    @RequestMapping(value = "/tiles/{name}/{z}/{x}/{y}.terrain")
    public ResponseEntity<byte[]> getTerrainData(@PathVariable("name") String name,
                                                 @PathVariable("z") int z,
                                                 @PathVariable("x") int x,
                                                 @PathVariable("y") int y) {

        return new ResponseEntity<>(service.getTileData(name, z, x, y), terrainHttpHeaders, HttpStatus.OK);

    }

}
