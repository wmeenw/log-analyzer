package Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Statistic {
    private final List<String> files = new ArrayList<>();
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalBytes = new AtomicLong(0);
    private final AtomicLong maxSize = new AtomicLong(0);
    private final List<Long> allSizes = Collections.synchronizedList(new ArrayList<>());
    private final Map<Integer, Integer> codes = new HashMap<>();
    private final Map<String, Integer> resources = new HashMap<>();
    private final Map<String, DateStatistic> dates = new HashMap<>();

    private static class DateStatistic{
        String date;
        String weekday;
        int count;
    }

    public void addFile(String file){
        files.add(file);
    }

    public void addEntry(LogEntry entry){
        totalRequests.incrementAndGet();

        long size = entry.size;
        totalBytes.addAndGet(size);
        allSizes.add(size);
        maxSize.updateAndGet(m -> Math.max(m, size));

        codes.merge(entry.status, 1, Integer::sum);
        resources.merge(entry.resource, 1, Integer::sum);

        if (entry.times != null){
            String date = entry.times.toLocalDate().toString();
            String weekday = entry.times.getDayOfWeek().toString();

            dates.computeIfAbsent(date, d ->{
                var ds = new DateStatistic();
                ds.date = d;
                ds.weekday = weekday;
                return ds;
            }).count++;
        }
    }

    public List<String> getFiles(){
        return files;
    }

    public long getTotalRequest(){
        return totalRequests.get();
    }

    public double getAvgSize() {
        return totalRequests.get() > 0 ? Math.round((totalBytes.get() / (double) totalRequests.get()) * 100.0) / 100.0 : 0;
    }

    public long getMaxSize(){
        return maxSize.get();
    }

    public double getPercentile(){
        if (allSizes.isEmpty()) return 0;
        List<Long> sorted = new ArrayList<>(allSizes);
        Collections.sort(sorted);
        int ind = (int) Math.ceil(95.0 / 100.0 * sorted.size()) - 1;
        return Math.round(sorted.get(Math.min(ind, sorted.size() - 1))* 100.0) / 100.0;
    }

    public Map<Integer, Integer> getTopCodes(){
        return codes.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<String, Integer> getTopResources(){
        return resources.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    public List<Map<String, Object>> getDateStats(){
        List<Map<String, Object>> res = new ArrayList<>();
        long total = getTotalRequest();

        dates.values().stream()
                .sorted((d1, d2) -> d1.date.compareTo(d2.date))
                .forEach(ds ->{
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("date", ds.date);
                    m.put("weekday", ds.weekday);
                    m.put("totalRequestsCount", ds.count);
                    m.put("totalRequestsPercentage", total > 0 ? Math.round(ds.count * 10000.0 / total) / 100.0 : 0);
                    res.add(m);
                });
        return res;
    }
}
