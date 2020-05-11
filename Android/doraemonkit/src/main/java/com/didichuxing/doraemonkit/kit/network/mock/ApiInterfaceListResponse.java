package com.didichuxing.doraemonkit.kit.network.mock;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Author: xuweiyu
 * Date: 2020/3/22
 * Description:
 */
public class ApiInterfaceListResponse implements Serializable {

    /**
     * errcode : 0
     * errmsg : 成功！
     * data : [{"index":0,"_id":14,"name":"公共分类","project_id":7,"desc":"公共分类","uid":11,"add_time":1585010911,"up_time":1585010911,"__v":0,"list":[{"edit_uid":0,"status":"undone","index":0,"tag":[],"_id":11,"method":"POST","catid":14,"title":"/v1/teachpoint/filter","path":"/v1/teachpoint/filter","project_id":7,"uid":11,"add_time":1585011094,"up_time":1585012609},{"edit_uid":0,"status":"done","index":0,"tag":["tag"],"_id":29,"method":"POST","catid":14,"title":"/test1","path":"/test1","project_id":7,"uid":11,"add_time":1585493398,"up_time":1588578072}]},{"index":0,"_id":28,"name":"/sousuo","project_id":7,"desc":null,"uid":11,"add_time":1585493537,"up_time":1585493537,"__v":0,"list":[{"edit_uid":0,"status":"undone","index":0,"tag":[],"_id":38,"method":"GET","catid":28,"title":"/test2","path":"/test2","project_id":7,"uid":11,"add_time":1585493563,"up_time":1585493607}]}]
     */

    private int errcode;
    private String errmsg;
    private List<DataBean> data;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * index : 0
         * _id : 14
         * name : 公共分类
         * project_id : 7
         * desc : 公共分类
         * uid : 11
         * add_time : 1585010911
         * up_time : 1585010911
         * __v : 0
         * list : [{"edit_uid":0,"status":"undone","index":0,"tag":[],"_id":11,"method":"POST","catid":14,"title":"/v1/teachpoint/filter","path":"/v1/teachpoint/filter","project_id":7,"uid":11,"add_time":1585011094,"up_time":1585012609},{"edit_uid":0,"status":"done","index":0,"tag":["tag"],"_id":29,"method":"POST","catid":14,"title":"/test1","path":"/test1","project_id":7,"uid":11,"add_time":1585493398,"up_time":1588578072}]
         */

        private int index;
        private int _id;
        private String name;
        private int project_id;
        private String desc;
        private int uid;
        private int add_time;
        private int up_time;
        private List<ListBean> list;

        public DataBean() {
        }

        public DataBean(String name, int id) {
            this._id = _id;
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public int getUp_time() {
            return up_time;
        }

        public void setUp_time(int up_time) {
            this.up_time = up_time;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * edit_uid : 0
             * status : undone
             * index : 0
             * tag : []
             * _id : 11
             * method : POST
             * catid : 14
             * title : /v1/teachpoint/filter
             * path : /v1/teachpoint/filter
             * project_id : 7
             * uid : 11
             * add_time : 1585011094
             * up_time : 1585012609
             */

            private int edit_uid;
            private String status;
            private int index;
            private int _id;
            private String method;
            private int catid;
            private String title;
            private String path;
            private int project_id;
            private int uid;
            private int add_time;
            private int up_time;
            private List<?> tag;

            public String getParams() {
                return params;
            }

            public void setParams(String params) {
                this.params = params;
            }

            private String params;

            public int getEdit_uid() {
                return edit_uid;
            }

            public void setEdit_uid(int edit_uid) {
                this.edit_uid = edit_uid;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int get_id() {
                return _id;
            }

            public void set_id(int _id) {
                this._id = _id;
            }

            public String getMethod() {
                return method;
            }

            public void setMethod(String method) {
                this.method = method;
            }

            public int getCatid() {
                return catid;
            }

            public void setCatid(int catid) {
                this.catid = catid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public int getProject_id() {
                return project_id;
            }

            public void setProject_id(int project_id) {
                this.project_id = project_id;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getAdd_time() {
                return add_time;
            }

            public void setAdd_time(int add_time) {
                this.add_time = add_time;
            }

            public int getUp_time() {
                return up_time;
            }

            public void setUp_time(int up_time) {
                this.up_time = up_time;
            }

            public List<?> getTag() {
                return tag;
            }

            public void setTag(List<?> tag) {
                this.tag = tag;
            }
        }
    }
}
