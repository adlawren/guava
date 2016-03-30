package ca.ualberta.papaya.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

/**
 * Created by adlawren on 28/03/16.
 */
public class ThrowawayElasticChangeSet {
    /**
     * return lowercase class name
     * @return type
     */
    private static String typeName(Class<?> kind){
        return kind.getSimpleName().toLowerCase();
    }

    private static String typeName(ElasticModel model){
        Class k = null;
        if (model instanceof Bid){ k = ((Bid) model).kind; }
        else if (model instanceof Photo){ k = ((Photo) model).kind; }
        else if (model instanceof Tag){ k = ((Tag) model).kind; }
        else if (model instanceof Thing){ k = ((Thing) model).kind; }
        else if (model instanceof User){ k = ((User) model).kind; }
        // return typeName(k); // TODO: Fix; causes null pointer exception
        return typeName(model.getClass());
    }

    public static class ElasticChangeSet {

        private static String changeListFile = "change_list.sav";

        private static List<ElasticModel> changeList = new ArrayList<ElasticModel>();

        public static Boolean contains(ElasticModel model) {
            return changeList.contains(model);
        }

        public static void add(ElasticModel model) {
            if (!changeList.contains(model)) {
                changeList.add(model);
            }

            commit();
        }

        public static Boolean committing = false;

        public static void commit() {
            if(!committing) {
                committing = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<ElasticModel> toRemove = new ArrayList<ElasticModel>();
                        for (int i = 0; i < changeList.size(); i++) {
                            ElasticModel model = changeList.get(i);
                            if (model.publish && !model.published) {
                                // insert
                                // todo: offline storage
                                model.published = true;
                                String type = typeName(model);

                                // TODO: Remove; test
                                System.out.println("Typename: " + type);

                                Index index = new Index.Builder(model)
                                        .index(ElasticModel.index)
                                        .type(type).build();

                                try {
                                    // String send = index.getData(new Gson());

                                    // JestResult resp = ElasticModel.getClient().execute(index);
                                    DocumentResult resp = ElasticModel.getClient().execute(index);
                                    if (!resp.isSucceeded()) {
                                        System.err.println("[ElasticModel.commit] " +
                                                "ERROR: REST push unsuccessful.");
                                    }

                                    // model.id = resp.getId();

                                    // String result = resp.getJsonString();

                                    toRemove.add(model);
                                } catch (IOException e){
                                    // todo: make more robust
                                    model.published = false;
                                    e.printStackTrace();
                                }
                            } else {
                                // update
                                // OMG PANIC. :j No "updates" allowed. k?

                                // todo: offline storage
                                model.published = true;
                                String type = typeName(model);

                                Index index = new Index.Builder(model)
                                        .index(ElasticModel.index)
                                        .type(type)
                                        .id(model.getId())
                                        .build();

                                try {
                                    JestResult resp = ElasticModel.getClient().execute(index);
                                    if (!resp.isSucceeded()) {
                                        System.err.println("[ElasticModel.commit] " +
                                                "ERROR: REST update unsuccessful.");
                                    }

                                    toRemove.add(model);
                                } catch (IOException e){
                                    // todo: make more robust
                                    model.published = false;
                                    e.printStackTrace();
                                }

                                // System.err.println("TODO: Implement updates.");
                            }
                        }

                        changeList.removeAll(toRemove);
                        committing = false;
                    }
                }).start();
            }
        }

        public static void saveChangeListFile(){

            return;

            // todo: offline
            /*
            Gson gson = new Gson();
            String s = gson.toJson(changeList);

            FileOutputStream outputStream;

            try {
                outputStream = Ctx.get().openFileOutput(changeListFile, Context.MODE_PRIVATE);
                outputStream.write(s.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            */
        }

        public static void loadChangeListFile(){
            return;

            // todo: offline
        }
    }
}